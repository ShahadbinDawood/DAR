# DAR

DAR is a smart home management backend system built with Spring Boot. The system helps users manage homes, home items, sensors, subscriptions, payments, notifications, bills, maintenance, and AI-powered maintenance support.

The backend includes subscription-based limits, JWT security, automatic subscription plan seeding, location lookup, nearby maintenance place suggestions, and AI maintenance recommendations.

## Extra Endpoints

| # | Feature | Method | Path | Description |
|---|---|---|---|---|
| 1 | Home | GET | `/api/v1/home/get/{id}` | Gets one home by id. |
| 2 | Home | GET | `/api/v1/home/get/user/{userId}` | Gets all homes owned by a user. |
| 3 | HomeItem | GET | `/api/v1/home-item/get/{id}` | Gets one home item by id. |
| 4 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}` | Gets all home items inside one home. |
| 5 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}/category/{category}` | Filters home items by category. |
| 6 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}/status/{status}` | Filters home items by status. |
| 7 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}/upcoming-service` | Gets items with service dates coming soon. |
| 8 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}/search` | Searches home items by name or brand using `keyword`. |
| 9 | HomeItem | GET | `/api/v1/home-item/get/home/{homeId}/summary` | Returns item dashboard summary for a home. |
| 10 | HomeItem | GET | `/api/v1/home-item/get/{itemId}/nearby-maintenance` | Finds nearby maintenance places related to the item. |
| 11 | HomeItem | GET | `/api/v1/home-item/get/{itemId}/ai-maintenance-advice` | Returns AI maintenance advice for the item. |
| 12 | HomeItem | POST | `/api/v1/home-item/get/{itemId}/ai-troubleshooting` | Returns AI troubleshooting steps based on the user's issue. |
| 13 | HomeItem | GET | `/api/v1/home-item/get/{itemId}/ai-nearby-recommendation` | Uses AI to recommend a suitable nearby service provider. |
| 14 | Location | GET | `/api/v1/location/geocode` | Converts an address into latitude and longitude using Nominatim. |
| 15 | UserSubscription | POST | `/api/v1/user-subscription/upgrade/{userId}/{planId}` | Creates a pending subscription upgrade for a user. |
| 16 | UserSubscription | GET | `/api/v1/user-subscription/user/{userId}` | Gets all subscriptions for a user. |
| 17 | Payment | GET | `/api/v1/payment/get/user/{userId}` | Gets all payments made by a user. |

