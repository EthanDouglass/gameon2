# gameon2
This is a project that I worked on with a team in my junior year in college at Iowa State. I was on the backend team. The app in a game that connects a host and players (up to 8) and they can either play a true or false game called "Fact Or Cap" or a drawing guessing game where one member is chosen to draw something and the others must guess what it is called "SketchIt".

For the database, we utilized MySQL on a Mariadb. We used Hibernate to manage the database with Springboot and created entities and repositories. There one-to-one, one-to-many, and many-to-many relationships implemented in this project.

Almost all data between frontend and backend is sent using HTTP traffic and JSON objects. We then establish a websocket connection when the lobby is joined.
