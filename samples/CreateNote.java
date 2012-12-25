import org.kender.simplenote.SNote;
import org.kender.simplenote.SimpleNote;
import org.kender.simplenote.exceptions.ConnectionFailed;

public class CreateNote {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You need more arguments");
            return;
        }
        
        String email = args[0];
        String password = args[1];
        
        SimpleNote sn = new SimpleNote(email, password);
        
        try {
            SNote note = sn.addNote("This is my first note");
            System.out.println(note);
        } catch (ConnectionFailed e) {
            e.printStackTrace();
        }
    }
}
