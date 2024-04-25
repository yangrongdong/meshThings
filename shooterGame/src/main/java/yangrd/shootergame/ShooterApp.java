package yangrd.shootergame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import kotlin.random.Random;

import java.net.URL;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ShooterApp extends GameApplication {

    //游戏窗口参数
    private final static String version = "V0.1";
    private final static int GAME_WIN_WIDTH = 800;
    private final static int GAME_WIN_HEIGHT = 600;

    //PLAYER 的参数
    private final static double PLAYER_WIDTH = 20;
    private final static double PLAYER_HEIGHT = 20;
    private static final double PLAYER_SPEED = 10;
    private static final double BULLET_WIDTH = 3;
    private static final double BULLET_HEIGHT = 3;

    Logger logger = Logger.get("shooter");
    public static void main(String[] args) {
        launch(args);
    }

    private enum EntityType{
        PLAYER, GOAL, BULLET
    }
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("射击小游戏");
        settings.setVersion(version);
        settings.setHeight(GAME_WIN_HEIGHT);
        settings.setWidth(GAME_WIN_WIDTH);
    }

    private Entity player;



    @Override
    protected void initGame() {
        Texture t = texture("青色の忍者.png");
        t.setFitWidth(20);
        t.setFitHeight(20);
        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(getAppWidth() / 2.0, getAppHeight() - PLAYER_HEIGHT)
                .viewWithBBox(t)
                .buildAndAttach();

        generateGoal(getRandomPoint());
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A,()->{
            double leftDistance = player.getX();
            player.translateX(-Math.min(leftDistance,PLAYER_SPEED));
        });
        onKey(KeyCode.D,()->{
            double rightDistance = getAppWidth() - player.getX() - PLAYER_WIDTH;
            player.translateX(Math.min(rightDistance, PLAYER_SPEED));
        });
        onKeyDown(KeyCode.J, ()->{
            shoot(player.getPosition());
        });
        onKey(KeyCode.U, ()->{
            shoot(player.getPosition());
        });
        onKeyDown(KeyCode.G,()->{
            generateGoal(getAppCenter());
        });
        onKey(KeyCode.Q, ()->{
            getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.BULLET));
        });
        onKeyDown(KeyCode.P, ()->{
            var goals = getGameWorld().getEntitiesByType(EntityType.GOAL);
            goals.forEach(e->logger.info(e.getPosition().toString()));
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        super.initGameVars(vars);
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(EntityType.BULLET, EntityType.GOAL) {
                    @Override
                    protected void onCollisionBegin(Entity a, Entity b) {
                        a.removeFromWorld();
                        logger.info("a hit");
                        generateGoal(getRandomPoint());
                        b.removeFromWorld();
                    }
                }
        );
    }

    @Override
    protected void onUpdate(double tpf) {
        var world = getGameWorld();
        var bullets = world.getEntitiesByType(EntityType.BULLET);
        bullets.forEach(e->{
            e.translateY(-10);
        });
        for(var bullet : bullets){
            if(bullet.getY() < 0) world.removeEntity(bullet);
        }
    }

    private void shoot(Point2D position){
        double bulletRelativePosition = PLAYER_WIDTH / 2 - BULLET_WIDTH / 2;
        entityBuilder()
                .type(EntityType.BULLET)
                .at(position.getX() + bulletRelativePosition,position.getY() - BULLET_HEIGHT)
                .viewWithBBox(new Rectangle(BULLET_WIDTH, BULLET_HEIGHT))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private void generateGoal(Point2D point2D){
        Texture t = texture("青紫のエイリアン.png");
        t.setFitWidth(20);
        t.setFitHeight(20);
        entityBuilder()
                .type(EntityType.GOAL)
                .at(point2D)
                .viewWithBBox(t)
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private Point2D getRandomPoint(){
        double x = Random.Default.nextDouble() * 700;
        double y = Random.Default.nextDouble() * 500;
        logger.info(x + "," + y);
        return new Point2D(x,y);
    }
}
