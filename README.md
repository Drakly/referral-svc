# referral-svc

referral-svc is a microservice for managing referral programs. This project provides a RESTful API for generating, validating, and tracking unique referral codes.

## Key Features

- **Code Generation:** Create unique referral codes.
- **Tracking:** Log and monitor code usage.
- **Integration:** Easy integration via RESTful API.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Drakly/referral-svc.git
   cd referral-svc
   ```
Install dependencies:
```
  npm install
```
Running the Service
To run locally:
```
  npm start
```
API Endpoints Examples
GET /api/referrals – List all referral codes.
POST /api/referrals – Create a new referral code.
GET /api/referrals/:id – Retrieve details for a specific code.
