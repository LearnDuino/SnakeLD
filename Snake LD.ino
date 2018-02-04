#include <IRremote.h>

IRrecv irrecv(11);
decode_results results;

void setup()
{
	Serial.begin(9600);
	irrecv.enableIRIn();
}

void loop()
{
	if (irrecv.decode(&results))
	{
		switch (results.value)
		{
		case 0xFF609F: // up 
		{
			Serial.println("UP");
			break;
		}
		case 0xFF22DD: // down 
		{
			Serial.println("DOWN");
			break;
		}
		case 0xFFE21D: // left 
		{
			Serial.println("LEFT");
			break;
		}
		case 0xFF02FD: // right
		{
			Serial.println("RIGHT");
			break;
		}
		case 0xFFE01F: // enter
		{
			Serial.println("ENTER");
			break;
		}
		}

		irrecv.resume();
	}

	delay(100);
}