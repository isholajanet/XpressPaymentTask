# XpressPaymentTask
This is a springboot application designed to handle payment transaction via a third-party API (Xpresspayment).

Features:
Payment Processing: The application supports the processing of payment
transactions, precisely for airtime purchases.

#### Technology used
Java
Springboot
RestTemplate
Postgresql
MockMvc
Mockito

## Getting Started
### Prerequisites
* Java 8+

* Maven
* Postgresql

### Build and run 
1. Clone the repository
    #### git clone https://github.com/isholajanet/XpressPaymentTask.git
    cd XpressPaymentTask

2. Build the Project
    
    #### mvn clean install

3. Run the application 

    #### mvn spring-boot:run
The application will be accessible at http://localhost:8080

## Configuration
Application properties are configured in the application.yml file. 
Important configurations include:

* Payment API URL: The URL for the third-party payment API.
* Private and Public Keys: Keys required for authentication.

### Usage
The primary functionality of the application is to process airtime payments. You can make payment requests through the /api/v1/payment/airtime endpoint.
A sample json request

    POST /api/v1/payment/airtime
    Content-Type: application/json
    Authorization: Bearer YOUR_PUBLIC_KEY
    PaymentHash: GENERATED_HASH
    
    {
    "requestId": "123456",
    "uniqueCode": "MTN_24207",
    "details": {
    "phoneNumber": "08033333333",
    "amount": 100
    }
    }

Response Sample

    {
    "requestId": "123456",
    "referenceId": "MATT14539722120213053702634214",
    "responseCode": "00",
    "responseMessage": "Successful",
    "data": {
        "phoneNumber": "08033333333"
        "amount": 100
        }
    }
