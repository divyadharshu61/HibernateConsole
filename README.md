Streaming Platform (Console App) — Hibernate + Oracle

A simple Java console application to manage Users, Subscriptions, and Payments using Hibernate ORM with Oracle 21c. This project supports:

Auto-creating users (no manual DB insert needed)
Creating subscriptions
Processing payments
Oracle 21c compatible SQL (no IDENTITY, no FETCH FIRST)

Tech Stack

Java (JDK 17+ recommended)
Hibernate ORM 6.x
Oracle Database 21c (21)
JDBC (Oracle Driver)
HikariCP (optional, if included)

Project Structure (Important Packages)
com.stream.bean → Entity classes (User, Subscription, Payment)
com.stream.DAO → DAO classes (UserDAO, SubscriptionDAO, PaymentDAO)
com.stream.service → Business logic (SubscriptionService)
com.stream.util → HibernateUtil + custom exceptions
com.stream.app → StreamMain (console entry point)
<img width="402" height="909" alt="image" src="https://github.com/user-attachments/assets/befc0f9b-bf91-4ea8-9503-0362338ed89a" />
<img width="859" height="970" alt="Screenshot 2026-02-21 215606" src="https://github.com/user-attachments/assets/f53b1abf-deb7-4844-abd7-75defba5032d" />
<img width="818" height="971" alt="Screenshot 2026-02-21 215631" src="https://github.com/user-attachments/assets/5464fe46-101e-4482-8cb0-7176d10717f4" />
<img width="821" height="925" alt="Screenshot 2026-02-21 215810" src="https://github.com/user-attachments/assets/2e00aaa8-7b81-47a9-9d8a-7ebf503fcacb" />


