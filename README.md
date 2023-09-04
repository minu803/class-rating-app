# class-rating-app

The project was to develop an interactive and comprehensive list of courses, complete with details such as the academic year, semester, department, course number, and title. Additionally, the app has a user-friendly rating feature, enabling users to rate individual courses. The project involved intricate client-server interactions, data transfer protocols, and UI design, all implemented in Java.

## Phase 1: Initial Course Summary
The starting page of the app displays a summarized list of courses. Instead of merely showing the course numbers, the list includes additional details like the academic year, semester, department, and title. The courses were initially unsorted and not presented in a useful way. To remedy this, I used Java's Comparator method to efficiently sort the list based on multiple criteria. This organized list serves as the base for the app, and its data is reused in various other sections, thereby enhancing the application's coherence and user experience.

## Phase 2: Detailed Course Information
Upon clicking on a specific course, a new activity is launched, showing detailed information about that course. This is achieved through a client-server interaction: when the new activity initiates, a request is sent to the back-end server, which hosts a comprehensive list of courses. The server, in turn, sends back the necessary information in JSON format, updating the activity’s display. This JSON data packet contains a variety of details, including a course description, allowing users to make informed decisions about their course selections.

## Phase 3: Course Rating Feature
One of the key features of the application is the ability to rate courses. A rating bar consisting of stars is presented in the detailed course information page. When a user rates a course by clicking on the stars, the information is sent back to the server for storage. The server is designed to identify the client device from which the rating originated, enabling it to handle ratings from multiple clients simultaneously. This feature required implementing an identification protocol, to ensure that ratings coming from 3 to 4 phones connected to the same server could be accurately distinguished and stored.
