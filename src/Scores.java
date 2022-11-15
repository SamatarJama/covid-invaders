
import java.io.*;
import java.util.*;

public class Scores {

    List<Integer> highScores = new ArrayList<Integer>();
    int score;

    public Scores(int score) {
        this.score = score;
    }

    //gets the current score and writes it down to the highscores file
    public void writeScoreToFile(){
        Writer newFile;
        try {
            newFile = new FileWriter("highscores.txt", true);
            newFile.write(score + "\r\n");
            newFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // reads the highscores file and puts the content in a highscores arraylist
    public void readScoreFile() {
        try {
            Scanner input = new Scanner(new File("highscores.txt"));

            while (input.hasNextLine()) {
                String[] x = input.nextLine().split("\r\n");
                highScores.add(Integer.parseInt(x[0]));
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //reads the highscores file and gets the top 5 and adds it to the topfive file
    protected void getTopFiveScores() {
        readScoreFile();
        Collections.sort(highScores, Collections.reverseOrder());

        try {
            //delete the content of the file so that the new top five can be added
            Writer newFile;
            newFile = new FileWriter("topfive.txt");
            newFile.write("");
            newFile.close();

            int i = 0;
            while (i < 5) {
                if(highScores.size()<=4)
                {
                    break;
                }
                Writer writer;
                writer = new FileWriter("Topfive.txt", true);
                writer.write(highScores.get(i) + "\r\n");
                writer.close();
                i++;
            }
        }
        catch (IndexOutOfBoundsException | IOException e){
        }

    }
}
