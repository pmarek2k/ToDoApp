# ToDo Application

Task management application

## Built with
- Java 11
- JUnit
- Log4j
- Jackson

## Description

This application is implementation of a web application for task management. The application allows you to create users and manage simple tasks (create, display, delete, ...)

Application:

1. provides resources and functionality via REST API
1. is available at [http://localhost:8080](http://localhost:8080/)
1. handles request contents in JSON format
1. returns the response in JSON format
1. handles the following HTTP requests
    * _method_ - HTTP method of the request,
    * _address_ - URL path of the requested resource,
    * _headers_ - variables sent in the request header,
    * _parameters_ - variables sent in the resource path,
    * _content_ - an example of the possible content of the request,
    * _responses_ - supported status codes and an example of the possible response content
  
    method | address | headers | parameters | body | responses
    ------ | ----- | -------- | --------- | ----- | ----------
    POST | /todo/user | | | <pre>{<br/>&#9;"username": "janKowalski",<br/>&#9;"password": "am!sK#123"<br/>}</pre> | <ul> <li>201</li><li>400</li><li>409</li> </ul>
    POST | /todo/task | auth | | <pre>{<br/>&#9;"description": "Kup mleko",<br/>&#9;"due": "2021-06-30"<br/>}</pre> | <ul><li>201<pre>{<br/>&#9;"id": "237e9877-e79b-12d4-a765-321741963000"<br/>}</li><li>400</li><li>401</li><ul>
    GET | /todo/task | auth | | | <ul><li>200<pre>[<br/>&#9;{<br/>&#9;&#9;"id": "237e9877-e79b-12d4-a765-321741963000",<br/>&#9;&#9;"description": "Kup mleko",<br/>&#9;&#9;"due": "2021-06-30"<br/>&#9;}<br/>]</pre></li><li>400</li><li>401</li></ul>
    GET | /todo/task/{id} | auth | id | | <ul><li>200<pre>{<br/>&#9;"id": "237e9877-e79b-12d4-a765-321741963000",<br/>&#9;"description": "Kup mleko",<br/>&#9;"due": "2021-06-30"<br/>}</pre></li><li>400</li><li>401</li><li>403</li><li>404</li></ul>
    PUT | /todo/task/{id} | auth | id | <pre>{<br/>&#9;"description": "Kup mleko",<br/>&#9;"due": "2021-06-30"<br/>}</pre> | <ul><li>200<pre>{<br/>&#9;"id": "237e9877-e79b-12d4-a765-321741963000",<br/>&#9;"description": "Kup mleko",<br/>&#9;"due": "2021-06-30"<br/>}</pre></li><li>400</li><li>401</li><li>403</li><li>404</li></ul>
    DELETE | /todo/task/{id} | auth | id |  | <ul><li>200</li><li>400</li><li>401</li><li>403</li><li>404</li></ul>
  
    * **auth** - strinb 'base64(username):base64(password)', where base64() is the Base64 encoding function. For example, for the user `{ "username": "janKowalski", "password": "am!sK#123" }`, `auth` will be`amFuS293YWxza2k=:YW0hc0sjMTIz`
    * **id** - unique identifier of the job in UUID format

1. The application returns a response with the code 404 (HTTP Not Found) for unsupported addresses
1. Tasks and users created using requests are remembered as part of a once-run application
1. The implementation includes unit tests that validate all supported requests
1. The application logs the most important information while the application is running

