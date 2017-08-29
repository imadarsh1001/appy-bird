package com.oobtalks.appy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture[] birds;
	Texture background,toptube,bottomtube,gameover;
    Music backMusic;
    //ShapeRenderer shapeRenderer;
	int FlapState=0;
    double velocity=0;
    float birdY=0;
    Circle birdCircle;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;
    int gamestatus=0;
    double gravity=1.33;
    float gap=400;
    Random random;
    //float maxTubeoffset;
    float tubeVelocity=4;
    int numberOfTube=4;
    float[] tubeX=new float[numberOfTube];
    float [] tubeoffset=new float[numberOfTube];
    float distanceBwTubes;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    @Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
        gameover = new Texture("gameover.png");
        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        backMusic=Gdx.audio.newMusic(Gdx.files.internal("ThemeMusic.mp3"));
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
		birds =new Texture[4];
		birds[0]= new Texture("frame-1.png");
		birds[1]= new Texture("frame-2.png");
		birds[2]= new Texture("frame-3.png");
		birds[3]= new Texture("frame-4.png");
        toptube=new Texture("toptube.png");
        bottomtube=new Texture("bottomtube.png");
        random=new Random();
        distanceBwTubes=Gdx.graphics.getWidth()*3/4;
        topTubeRectangles = new Rectangle[numberOfTube];
        bottomTubeRectangles = new Rectangle[numberOfTube];


        startGame();


	}

    public void startGame() {

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTube; i++) {

            tubeoffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBwTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }


    }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
       if (gamestatus==1)
       {
           backMusic.play();
           if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

               score++;

               Gdx.app.log("Score", String.valueOf(score));

               if (scoringTube < numberOfTube- 1) {

                   scoringTube++;

               } else {

                   scoringTube = 0;

               }

           }
           if(Gdx.input.justTouched())
           {
               velocity=-20;

           }

           for(int i=0;i<numberOfTube;i++)
           {
               if (tubeX[i]<-toptube.getWidth())
               {
                   tubeX[i]+=numberOfTube*distanceBwTubes;
                   tubeoffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
               }
               else {
                   tubeX[i] -= tubeVelocity;
               }
               batch.draw(toptube,tubeX[i], Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i]);
               batch.draw(bottomtube,tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight()+tubeoffset[i]);
               topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], toptube.getWidth(), toptube.getHeight());
               bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
           }



           if(birdY > 0 )
           {
               velocity=velocity+gravity;
               birdY-=velocity;
           }
           else {
               gamestatus=2;
           }

       }
       else if (gamestatus==0){
           if(Gdx.input.justTouched())
           {
               gamestatus=1;

           }
       }

       else if (gamestatus == 2) {

           batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
            backMusic.stop();
           if (Gdx.input.justTouched()) {

               gamestatus = 1;
               startGame();
               score = 0;
               scoringTube = 0;
               velocity = 0;

           }

       }

        for(int i=0;i<5 && gamestatus==1;i++){
            FlapState+=1;
            if(FlapState==4){
                FlapState=0;
            }
        }


		batch.draw(birds[FlapState],Gdx.graphics.getWidth()/ 2 - birds[FlapState].getWidth()/2,birdY);
        font.draw(batch, String.valueOf(score), 100, 200);

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[FlapState].getHeight() / 2, birds[FlapState].getWidth() / 2);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfTube; i++) {

            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

                gamestatus= 2;

            }

        }
        batch.end();
        //shapeRenderer.end();
	}

}
