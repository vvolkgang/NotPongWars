package com.notpongwars.game;

public class CollisionSystem {

	private Paddle p1, p2;

	public CollisionSystem(Paddle p1, Paddle p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public void update() {
		Bullet bA = p1.getBullet();
		Bullet bB = p2.getBullet();

		if(bA.isActive())
			if(isBallColliding(bA, p2))
				bA.onCollideWith(p2);

		if(bB.isActive())
			if(isBallColliding(bB, p1))
				bB.onCollideWith(p1);
	}

	private boolean isBallColliding(Bullet bullet, Paddle enemy) {
		return bullet.getBoudingBox().intersect(enemy.getBoudingBox());
	}

}
