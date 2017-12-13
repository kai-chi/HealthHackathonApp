# HealthHackathonApp
App written in 24h for Health Hackathon in Berlin http://hacking-health.org/berlin/bhi-hackathon2017/

What does the app do? It connects to glucose/insulin sensor via Bluetooth and receives the results, which are passed to the back-end 
server where all the machine learning magic happens. Ideally, the server replies with results evaluation and diet/fitness recommendations, 
which the user can integrate into the calendar.

!!! App is partially mocked for the purpose of the presentation and because of lack of time. 
What is mocked?
- connection with bluetooth device (lack of time / hardware part was not completed)
- getting results from bluetooth device (obviously)
- recommendation list (back-end did not support recommendations at the time of finishing the app, so there was no sense in implementing it)

As long as the server is online the app will not crash :) 

Lot of things could be improved but it's highly unlikely that the app will be developed. Maybe some code refactoring if I'll have time.

***********
 HOWTO 
***********
1. Enable bluetooth
2. Click the button to start discovering devices
3. Long click the same button to proceed to the next step
4. Click on the white drop to send mocked data to the server
5. Click on pictures of activities to add them to your calendar (they're all added on 12th of December 2017) :)
6. Go jogging :)

<img src="https://raw.githubusercontent.com/kai-chi/HealthHackathonApp/master/screenshots/Screenshot_20171213-220750.png" width="250"> <img src="https://raw.githubusercontent.com/kai-chi/HealthHackathonApp/master/screenshots/Screenshot_20171213-220802.png" width="250"> <br />
<img src="https://raw.githubusercontent.com/kai-chi/HealthHackathonApp/master/screenshots/Screenshot_20171213-220809.png" width="250"> <img src="https://raw.githubusercontent.com/kai-chi/HealthHackathonApp/master/screenshots/Screenshot_20171213-220818.png" width="250">
