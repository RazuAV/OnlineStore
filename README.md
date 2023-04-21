## Wantsome - Project OnlineStore

---

### 1. Description

This is an application that I am still working on, that manages the backend of an OnlineStore. I started to work on this project during my Java Course at Wantsome Academy and the purpose of this project is to create an Online Store app which has features like register/login, searching for products, adding products to cart, checkout for current order with options for payment like cash or card, listing orders for clients.

---

### 2. Setup

In order to start the application, you have to run the OnlineStore class from src/main/java/project. You also have to edit the ConnectionManager class in order to set up your path for the db. When the application runs, it will also create the database(if does not exist) which consists of 4 tables(clients, products, orders and orderItems) and will also insert some data in clients and products tables. 

---

### 3. Technical details

__Technologies__

- main code is written in Java (version 11)
- it uses [SQLite](https://www.sqlite.org/), a small embedded database, for its
  persistence, using SQL and JDBC to access it from Java code
- it uses [Javalin](https://javalin.io/) micro web framework (which includes
  an embedded web server, Jetty)
- it uses [Velocity](https://velocity.apache.org/) templating engine,
  to separate the UI code from Java code; UI code consists of basic HTML and CSS
  code. Used Bootstrap aswell. 


---




