import org.kender.simplenote.SNote;
import org.kender.simplenote.SimpleNote;
import org.kender.simplenote.exceptions.ConnectionFailed;

public class EmptyTrash {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You need more arguments");
            return;
        }
        
        String email = args[0];
        String password = args[1];
        
        SimpleNote sn = new SimpleNote(email, password);
        
        try {
            SNote[] notes = sn.getNoteList(false);
            for (SNote note: notes) {
                if (note.inTrash()) {
                    sn.deleteNote(note);
                    System.out.println("Deleting: " + note);
                }
            }
        } catch (ConnectionFailed e) {
            e.printStackTrace();
        }
    }
}
