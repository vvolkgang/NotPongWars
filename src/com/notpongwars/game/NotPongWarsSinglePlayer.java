package com.notpongwars.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.notpongwars.core.GamePanel;
import com.notpongwars.core.InputManager;
import com.notpongwars.game.Paddle.PaddleSide;
import com.notslip.notpongwars.R;

public class NotPongWarsSinglePlayer extends GamePanel {

	private static final int NET_COLOR = Color.GRAY;
	private static final int SCORE_COLOR = Color.GRAY;

	protected boolean isGameOver = false;

	private Paint paint;

	protected Point screenSize;

	protected Player leftPaddle;
	protected Player rightPaddle;
	protected Ball ball;

	protected CollisionSystem collisionSystem;

	protected int endScore;


	protected GameSettings settings;
	/**
	 * Gets the current game settings.
	 * @return Game Settings.
	 */
	protected GameSettings getGameSettings() { return this.settings; }


	public NotPongWarsSinglePlayer(Context context, GameSettings settings) {
		super(context);

		this.settings = settings;

		this.paint = new Paint();
	}

	@Override
	public void onInitialize() {
		Resources res = getResources();
		
		screenSize = new Point(getWidth(), getHeight());
		this.endScore = settings.getEndScore();
		

		initializeBall(res);
		initializeLeftPlayer(res);
		initializeRightEnemy(res);

		ball.addPaddle(leftPaddle);
		ball.addPaddle(rightPaddle);

		collisionSystem = new CollisionSystem(leftPaddle, rightPaddle);
	}

	@Override
	public void onUpdate() {
		if(!isGameOver) {
			checkGameOver();

			leftPaddle.update();
			rightPaddle.update();
			ball.update();

			collisionSystem.update();
			
			
		} else {
			if(InputManager.isTouchDown()) close();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {		
		if(!isGameOver) {
			drawBackground(canvas);
			drawScore(canvas);

			leftPaddle.draw(canvas);
			rightPaddle.draw(canvas);
			ball.draw(canvas);
		} else drawGameOverScreen(canvas);
	}

	protected void drawBackground(Canvas canvas) {
		paint.setColor(NET_COLOR);

		int netWidth = (int)(screenSize.x * 0.01f);
		int netX = (screenSize.x / 2) - netWidth / 2;
		canvas.drawRect(new Rect(netX, 0, netX + netWidth, screenSize.y), paint);
	}

	private void drawScore(Canvas canvas) {
		paint.setColor(SCORE_COLOR);

		paint.setTextSize(screenSize.x / 4);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("" + leftPaddle.getScore(), getWidth() / 4, getHeight() / 2 + 0, paint);
		canvas.drawText("" + rightPaddle.getScore(), getWidth() - (getWidth() / 4), getHeight() / 2 + 0, paint);
	}

	protected void checkGameOver() {
		if(leftPaddle.getScore() >= settings.getEndScore() || rightPaddle.getScore() >= settings.getEndScore()) {
			//close();
			this.isGameOver = true;
		}
	}

	/**
	 * Initialize the ball.
	 * @requires leftPaddle and rightPaddle to be initialized first.
	 * @param res
	 */
	protected void initializeBall(Resources res){
		ball = new Ball(screenSize, settings);
		ball.load(res);
		//ball.addPaddle(leftPaddle); ball.addPaddle(rightPaddle);
	}

	protected void initializeDummyBall(Resources res){
		ball = new DummyBall(screenSize, settings);
		ball.load(res);
	}

	protected void initializeLeftPlayer(Resources res){
		leftPaddle = new Player(PaddleSide.LEFT, screenSize, settings);
		leftPaddle.load(res);
	}

	protected void initializeRightPlayer(Resources res){
		rightPaddle = new Player(PaddleSide.RIGHT, screenSize, settings);
		rightPaddle.load(res);
	}

	protected void initializeLeftDummyPlayer(Resources res){
		leftPaddle = new DummyPlayer(PaddleSide.LEFT, screenSize);
		leftPaddle.load(res);
	}

	protected void initializeRightDummyPlayer(Resources res){
		rightPaddle = new DummyPlayer(PaddleSide.RIGHT, screenSize);
		rightPaddle.load(res);
	}

	private void initializeRightEnemy(Resources res){
		rightPaddle = new Enemy(PaddleSide.RIGHT, screenSize, ball, settings.getEnemyPaddleSpeed(), screenSize.x * settings.getEnemyAwarenessMultiplier() );

		rightPaddle.load(res);
	}

	private void drawGameOverScreen(Canvas canvas) {
		paint.setColor(Color.WHITE);

		paint.setTextSize(screenSize.x / 8);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("Game Over", getWidth() / 2, getHeight() / 2 + 0, paint);
		paint.setTextSize(screenSize.x / 10);
		canvas.drawText("Player " + (leftPaddle.getScore() >= endScore ? "Left" : "Right") + " Won", getWidth() * .5f, getHeight() - getHeight() / 4, paint);
	}

}
