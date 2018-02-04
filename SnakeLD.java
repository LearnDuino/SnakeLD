package snakeld;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.serial.Serial;

public class SnakeLD extends PApplet {

	int time_break = 200, start = millis(), lf = 10, point = 0, max_score = 0;
	Serial myPort;

	String com = null, v = "DOWN";
	ArrayList<PVector> vectors = new ArrayList<PVector>();
	PVector bonus = new PVector(5, 5);
	boolean moved = false, game = true;

	public static void main(String _args[]) {
		PApplet.main(new String[] { SnakeLD.class.getName() });
	}

	void border() {
		if (vectors.get(0).x > 17) {
			vectors.get(0).x = 0;
		}
		if (vectors.get(0).x < 0) {
			vectors.get(0).x = 17;
		}

		if (vectors.get(0).y > 17) {
			vectors.get(0).y = 0;
		}
		if (vectors.get(0).y < 0) {
			vectors.get(0).y = 17;
		}
	}

	boolean pauses() {
		if (millis() - start >= time_break) {
			start = millis();
			return true;
		} else {
			return false;
		}
	}

	void colision() {

		for (int i = 1; i < vectors.size(); i++) {

			if (vectors.get(i).x == vectors.get(0).x
					&& vectors.get(i).y == vectors.get(0).y) {
				game = !game;
			}
		}
	}

	void getPoint() {
		if (bonus.x == vectors.get(0).x && bonus.y == vectors.get(0).y) {
			point = point + 1;

			bonus = new PVector((int) random(0, 16), (int) random(0, 16));

			for (int i = 0; i < vectors.size(); i++) {
				if (bonus.x == vectors.get(i).x && bonus.y == vectors.get(i).y) {

					bonus = new PVector((int) random(0, 16),
							(int) random(0, 16));

					i = 0;

				}
			}

			vectors.add(new PVector(vectors.get(vectors.size() - 1).x, vectors
					.get(vectors.size() - 1).y));
		}
	}

	public void keybord() {

		while (myPort.available() > 0) {

			com = myPort.readStringUntil(lf).trim();

			if (com != null) {

				if (com.equals("UP") && v != "DOWN" && v != "UP" && !moved) {
					v = "UP";
					moved = true;
				}
				if (com.equals("DOWN") && v != "UP" && v != "DOWN" && !moved) {
					v = "DOWN";
					moved = true;
				}
				if (com.equals("LEFT") && v != "RIGHT" && v != "LEFT" && !moved) {
					v = "LEFT";
					moved = true;
				}
				if (com.equals("RIGHT") && v != "LEFT" && v != "RIGHT"
						&& !moved) {
					v = "RIGHT";
					moved = true;
				}
				if (com.equals("ENTER")) {
					vectors.clear();
					vectors.add(new PVector(0, 2));
					vectors.add(new PVector(0, 1));
					vectors.add(new PVector(0, 0));
					v = "DOWN";

					if (point > max_score) {
						max_score = point;
					}

					point = 0;
					game = true;

				}
			}
		}
	}

	void moveLoop() {
		if (v == "UP") {
			vectors.add(0, new PVector(vectors.get(0).x, vectors.get(0).y - 1));
			vectors.remove(vectors.size() - 1);
			moved = false;
		}
		if (v == "DOWN") {
			vectors.add(0, new PVector(vectors.get(0).x, vectors.get(0).y + 1));
			vectors.remove(vectors.size() - 1);
			moved = false;
		}
		if (v == "LEFT") {
			vectors.add(0, new PVector(vectors.get(0).x - 1, vectors.get(0).y));
			vectors.remove(vectors.size() - 1);
			moved = false;
		}
		if (v == "RIGHT") {
			vectors.add(0, new PVector(vectors.get(0).x + 1, vectors.get(0).y));
			vectors.remove(vectors.size() - 1);
			moved = false;
		}
	}

	void draw_stuff(int b_r, int b_g, int b_b, int h_r, int h_g, int h_b,
			int t_r, int t_g, int t_b, int p_r, int p_g, int p_b) {
		noStroke();
		background(b_r, b_g, b_b);

		for (int i = vectors.size() - 1; i >= 0; i--) {
			if (i == 0) {
				fill(h_r, h_g, h_b);
			} else {
				fill(t_r, t_g, t_b);
			}

			rect(vectors.get(i).x * 32, vectors.get(i).y * 32, 32, 32);
		}

		fill(p_r, p_g, p_b);
		rect(bonus.x * 32, bonus.y * 32, 32, 32);
	}

	public void settings() {
		size(576, 576); // 576 x 576
	}

	public void setup() {
		color(123, 0, 0);
		vectors.add(new PVector(0, 2));
		vectors.add(new PVector(0, 1));
		vectors.add(new PVector(0, 0));
		myPort = new Serial(this, Serial.list()[0], 9600);
		myPort.clear();
		textFont(createFont("Arial", 64, true));

	}

	public void draw() {

		keybord();

		if (game) {
			if (pauses()) {
				moveLoop();
			}

			colision();

			border();
			getPoint();

			// background r g b
			// head r g b
			// body r g b
			// point r g b

			color(0); // clear
			draw_stuff(204, 255, 153, 204, 102, 0, 0, 153, 0, 153, 0, 0);

			// white
			// blue
			// green
			// red

		} else {
			color(0); // clear
			draw_stuff(229, 204, 255, 153, 204, 204, 229, 255, 255, 255, 255,
					102);

			fill(255, 128, 0);
			textAlign(CENTER, TOP);
			text("Best Score:" + max_score, 288, 200);
			textAlign(CENTER, BOTTOM);
			text("Your Score:" + point, 288, 400);

			// red
			// blue
			// purple
			// blue
		}

		delay(100);
	}
}
