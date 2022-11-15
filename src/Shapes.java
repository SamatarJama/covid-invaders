
abstract class  Shapes {
    //holds all the values that are used the game object classes
    int x;
    int y;
    int velocity;
    boolean moveRight;
    boolean moveLeft;
    boolean moveUp;

    public Shapes(int x, int y, int velocity){
        this.x= x;
        this.y=y;
        this.velocity=velocity;
    }
}
