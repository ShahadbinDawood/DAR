package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.BillMonthlyReportDtoOut;
import com.example.DAR.DTO.Out.PurchaseInvoiceStatsDtoOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfReportService {

    private final HomeRepository homeRepository;
    private final Billservice billService;
    private final PurchaseInvoiceService purchaseInvoiceService;
    private final AiService aiService;

    public byte[] generateMonthlyReport(Integer homeId, int year, int month) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");

        List<BillMonthlyReportDtoOut> bills = billService.getMonthlyReport(homeId, year, month);
        PurchaseInvoiceStatsDtoOut stats = purchaseInvoiceService.getStatsByHome(homeId);

        String billsData = bills.isEmpty() ? "لا توجد فواتير" :
                bills.stream().map(b -> b.getType() + ": استهلاك=" + b.getTotalConsumption() + ", مبلغ=" + b.getTotalAmount() + " ريال")
                        .collect(Collectors.joining("\n"));

        String purchasesData = stats.getByCategory().isEmpty() ? "لا توجد مشتريات" :
                stats.getByCategory().stream().map(c -> c.getCategory() + ": " + c.getTotalAmount() + " ريال (" + c.getCount() + " فاتورة)")
                        .collect(Collectors.joining("\n"));

        String aiSummary = aiService.generateMonthlyReportSummary(home.getAddress(), year, month, billsData, purchasesData);

        String html = buildHtml(home.getAddress(), year, month, bills, stats, aiSummary);

        try {
            // نسخ الخط لملف مؤقت عشان ITextRenderer يقدر يوصله
            Path fontPath = extractFont();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(fontPath.toString(), true);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ApiException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private Path extractFont() throws Exception {
        InputStream is = getClass().getResourceAsStream("/fonts/SFArabic.ttf");
        if (is == null) throw new ApiException("Arabic font not found in resources");
        Path tmp = Files.createTempFile("SFArabic", ".ttf");
        Files.copy(is, tmp, StandardCopyOption.REPLACE_EXISTING);
        tmp.toFile().deleteOnExit();
        return tmp;
    }

    private String markdownToHtml(String text) {
        return text
                .replaceAll("(?m)^### (.+)$", "<h3>$1</h3>")
                .replaceAll("(?m)^## (.+)$", "<h2>$1</h2>")
                .replaceAll("(?m)^# (.+)$", "<h1>$1</h1>")
                .replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>")
                .replaceAll("(?m)^- (.+)$", "<li>$1</li>")
                .replaceAll("---", "<hr/>")
                .replace("\n", "<br/>");
    }

    private String buildHtml(String address, int year, int month,
                              List<BillMonthlyReportDtoOut> bills,
                              PurchaseInvoiceStatsDtoOut stats,
                              String aiSummary) {

        StringBuilder billRows = new StringBuilder();
        for (BillMonthlyReportDtoOut b : bills) {
            billRows.append("<tr>")
                    .append("<td>").append(translateType(b.getType())).append("</td>")
                    .append("<td>").append(b.getTotalConsumption()).append("</td>")
                    .append("<td>").append(b.getTotalAmount()).append(" ريال</td>")
                    .append("<td>").append(b.getBillCount()).append("</td>")
                    .append("</tr>");
        }

        StringBuilder categoryRows = new StringBuilder();
        for (PurchaseInvoiceStatsDtoOut.CategoryStatDtoOut c : stats.getByCategory()) {
            categoryRows.append("<tr>")
                    .append("<td>").append(c.getCategory()).append("</td>")
                    .append("<td>").append(c.getTotalAmount()).append(" ريال</td>")
                    .append("<td>").append(c.getCount()).append("</td>")
                    .append("</tr>");
        }

        String aiHtml = markdownToHtml(aiSummary);

        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
              <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
              <style>
                body { font-family: Arial, sans-serif; color: #3E302A; margin: 40px; font-size: 13px; }
                h1 { color: #765345; font-size: 22px; border-bottom: 2px solid #765345; padding-bottom: 8px; }
                h2 { color: #765345; font-size: 15px; margin-top: 24px; }
                h3 { color: #765345; font-size: 13px; margin-top: 10px; }
                table { width: 100%%; border-collapse: collapse; margin-top: 10px; }
                th { background-color: #765345; color: white; padding: 8px; text-align: left; }
                td { border: 1px solid #E2CFC3; padding: 8px; }
                tr:nth-child(even) { background-color: #FFF8F4; }
                .ai-box { background-color: #FFF8F4; border: 1px solid #E2CFC3; padding: 16px; margin-top: 16px; line-height: 1.8; }
                .footer { margin-top: 40px; text-align: center; color: #A68972; font-size: 11px; }
              </style>
            </head>
            <body>
              <h1>Monthly Report - DAR Platform</h1>
              <p><strong>Home:</strong> %s    <strong>Period:</strong> %d/%d</p>

              <h2>Utility Bills</h2>
              <table>
                <tr><th>Type</th><th>Consumption</th><th>Amount</th><th>Count</th></tr>
                %s
              </table>

              <h2>Purchases by Category</h2>
              <table>
                <tr><th>Category</th><th>Total</th><th>Count</th></tr>
                %s
              </table>

              <h2>AI Analysis</h2>
              <div class="ai-box">%s</div>

              <div class="footer">Auto-generated by DAR Platform - %d/%d</div>
            </body>
            </html>
            """.formatted(address, month, year, billRows, categoryRows, aiHtml, month, year);
    }

    private String translateType(String type) {
        return switch (type.toUpperCase()) {
            case "ELECTRICITY" -> "Electricity";
            case "WATER" -> "Water";
            case "GAS" -> "Gas";
            default -> type;
        };
    }
}
