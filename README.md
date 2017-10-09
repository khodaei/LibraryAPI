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
The service should now be up and running on http://localhost:8080.

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
Date: Mon, 09 Oct 2017 21:07:16 GMT

[{"id":1,"title":"Power of Habits","author":"Amir Khodaei"},{"id":2,"title":"title2","author":"author2"}]
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
Date: Mon, 09 Oct 2017 21:16:08 GMT

[{"bookId":1,"title":"Power of Habits","author":"Amir Khodaei","status":"UNREAD"},{"bookId":3,"title":"Title 1","author":"author","status":"UNREAD"}]
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
Request:
```
curl -i 'localhost:8080/users/amir/books?status=read'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:24:10 GMT

[]
```
Request:
```
curl -i 'localhost:8080/users/amir/books?status=unread'
```
Response:
```
```
#### list books by both author and status
Request:
```
curl -i 'localhost:8080/users/amir/books?author=andrew hunt&status=unread'
```
Response:

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
list books:
```
```
### mark book
Mark as read request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/users/amir/books/2 -d '{"status": "read"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:33:51 GMT

{"bookId":2,"title":"title2","author":"author2","status":"READ"}
```
Mark as unread request:
```
curl -i -X POST -H "Content-Type:application/json" localhost:8080/users/amir/books/2 -d '{"status": "unread"}'
```
Response:
```
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 09 Oct 2017 21:35:01 GMT

{"bookId":2,"title":"title2","author":"author2","status":"UNREAD"}
```
