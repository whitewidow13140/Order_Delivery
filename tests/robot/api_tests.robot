*** Settings ***
Library           RequestsLibrary

*** Variables ***
${ORDER_MANAGER}  http://localhost:8081
${DELIVERY_TRACKER}  http://localhost:8082

*** Test Cases ***
Create Order And Verify Delivery
    Create Session    order    ${ORDER_MANAGER}
    POST On Session   order    /orders    json={"item":"Laptop","quantity":1}
    Sleep    2s
    Create Session    delivery    ${DELIVERY_TRACKER}
    ${resp}=    GET On Session    delivery    /deliveries
    Should Contain    ${resp.text}    Laptop