# LibraryAPI
A web service for creating users and books and manipulating user book libraries.

## Getting Started
Download the project:
```
git clone git@github.com:khodaei/LibraryAPI.git
cd LibraryAPI
```
Build with gradle or the provided wrapper:
```
./gradlew build
```
Run the Jar file:
```
java -jar build/libs/LibraryAPI-0.0.1-SNAPSHOT.jar
```
The service should now be up and running on http://localhost:8080/users/.

## Example requests:

### list users
Request:
```
curl -i localhost:8080/users/
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:05:36 GMT

[{"username":"amir"}]
```
### list books
Request:
```
curl -i localhost:8080/books/
```
Response
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:54:25 GMT

[{"id":1,"title":"The Power of Habit","author":"Charles Duhigg"},{"id":2,"title":"The Pragmatic Programmer","author":"Andrew Hunt"}]
```
### create user 
Request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/users/ -d '{"username": "user2"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:09:26 GMT

{"username":"user2"}
```
### create book
Request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/books/ -d '{"title": "title2", "author": "author2"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:11:03 GMT

{"id":3,"title":"title2","author":"author2"}
```

### add book to user library
Request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/users/amir/books -d '{"bookId": "3"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:13:58 GMT

{"bookId":3,"title":"Title 1","author":"author","status":"UNREAD"}
```
### list books in user library
#### list all books
Request:
```
curl -i localhost:8080/users/amir/books
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:56:06 GMT

[{"bookId":1,"title":"The Power of Habit","author":"Charles Duhigg","status":"UNREAD"},{"bookId":3,"title":"title2","author":"author2","status":"UNREAD"}]
```
#### list books by author
Request:
```
curl -i 'localhost:8080/users/amir/books?author=author2'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:22:14 GMT

[{"bookId":3,"title":"title2","author":"author2","status":"UNREAD"}]
```
#### list books by status
List unread books request:
```
curl -i 'localhost:8080/users/amir/books?status=unread'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:57:57 GMT

[{"bookId":1,"title":"The Power of Habit","author":"Charles Duhigg","status":"UNREAD"},{"bookId":3,"title":"title2","author":"author2","status":"UNREAD"}]
```
Mark one book as read and list read books:
```
curl -i -X PUT -H "Content-Type:application/json" localhost:8080/users/amir/books/3 -d '{"status": "read"}'
curl -i 'localhost:8080/users/amir/books?status=read'
```
Response: 
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 22:10:06 GMT

[{"bookId":3,"title":"title2","author":"author2","status":"READ"}]
```
#### list books by both author and status
Request:
```
curl -i 'http://localhost:8080/users/amir/books?author=charles%20duhigg&status=unread'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 22:02:07 GMT

[{"bookId":1,"title":"The Power of Habit","author":"Charles Duhigg","status":"UNREAD"}]
```
### delete book from user library
Request:
```
curl -i -X DELETE localhost:8080/users/amir/books/1
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:30:04 GMT
```
list books after deleting book with bookId=1:
```
curl -i localhost:8080/users/amir/books
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 22:05:00 GMT

[{"bookId":3,"title":"title2","author":"author2","status":"UNREAD"}]
```
### mark book
Mark as read request:
```
curl -i -X PUT -H "Content-Type:application/json" localhost:8080/users/amir/books/3 -d '{"status": "read"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 22:05:58 GMT

{"bookId":3,"title":"title2","author":"author2","status":"READ"}
```
Mark as unread request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/users/amir/books/3 -d '{"status": "unread"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:35:01 GMT

{"bookId":2,"title":"title2","author":"author2","status":"UNREAD"}
```
