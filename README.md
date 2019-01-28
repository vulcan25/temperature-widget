My first (crap) attempt at an Andorid application who's only purpose is to display a widget showing the temperature from a Raspberry Pi.  It actually pulls the data from a Flask endpoint.

Endpoint returns this, with emojis indicating the health of the Pi:

	{"names":"2.562 \ud83e\udd15"}

A chilly 2.5 degress at my house, is displayed like this on the phone:

![screencap](https://i.imgur.com/CITFa0u.png)

The guy with the bandage means the Pi aint healthy, but that's another story.


----


The functionality of this was heavily based on the blog [Kotlin with Volley](https://www.varvet.com/blog/kotlin-with-volley/) by Daniell Algar.

And further shaped by 0X0nosugar on a stack exchange [thread](https://stackoverflow.com/questions/53658883/explain-this-basic-kotlin-function)

Thanks both. 


