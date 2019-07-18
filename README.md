# Indoor-Field-Reservation-Management-System
Windows Application that served as a reservation management system for an Indoor Soccer field rental center

Introduction
------------
The project was inspired by another one of my passion's, Soccer. The indoor soccer rental center I used to call and reserve a field at every weekend would constantly forget our reservation and had absolutely no organization to their reservations other than word of mouth from employees. Their idea of record keeping was writing it down on a sticky-note or scrap of paper. So it motivated me to create this Reservation Management system so that they could properly keep track of people's reservations.

Application Overview
------------
This app aimed to create a CRUD interface where an employee could store all the reservations for a field(their times, dates, user info, etc) all in an MySQL Database with a simple and easy to use UI for people that were most likely not tech savy.
* Each Customer's information could be viewed/added/deleted from database which included their contact information(email and phone)
* You could view, add and remove Reservation Openings for certain days(for example, they could add a opening tomorrow at 8pm that was open for reservation)
* You could view, add, and remove any Reservations any Openings that were added to database
* Only employees with a secure login and password combination could log into the application to modify or view the reservations, openings and customer data

MySQL Database Setup:
* Connected through the ip/socket combination for the local MySQL Server and using the MySQL API to connect to the database
* The database was a relational SQL database based on the following ER diagram/schema:
<img src="https://user-images.githubusercontent.com/39919952/61493623-a96ada80-a979-11e9-8aba-4ec9cf1f0717.png" alt="Screenshot"/> 

Technologies Used
-------------
* Netbeans v8.1
* Java SDK Development Kit
* MySQL Community Server v5.7.9
* TextPad

Screenshots
-------------
<img src="https://user-images.githubusercontent.com/39919952/61493180-94da1280-a978-11e9-892b-1b3da9274fac.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493181-973c6c80-a978-11e9-811e-90b4d96f1807.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493191-9e637a80-a978-11e9-94a0-702b18dfd804.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493218-a7544c00-a978-11e9-8ad2-5d0eeb2017c7.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493220-a91e0f80-a978-11e9-8abd-59aa87b83c49.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493228-ae7b5a00-a978-11e9-8b29-02c145bedfa4.png" height="400" alt="Screenshot"/> 
<img src="https://user-images.githubusercontent.com/39919952/61493229-af13f080-a978-11e9-861e-712ffd382007.png" height="400" alt="Screenshot"/> 




More Information
-------
For for in-depth information, refer to the INDOOR PROJECT REPORT.pdf above.
