import org.kender.simplenote.SNote;
import org.kender.simplenote.SimpleNote;
import org.kender.simplenote.exceptions.ConnectionFailed;

public class MoveToTrash {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You need more arguments");
            return;
        }
        
        String email = args[0];
        String password = args[1];
        
        SimpleNote sn = new SimpleNote(email, password);
        
        try {
            // Create a note first (or get it if we know the ID)
            SNote note = sn.addNote("This note will end up in the trash");
            
            note.trash(); // Flag it as "trashed"
            sn.updateNote(note); // Update the server
        } catch (ConnectionFailed e) {
            e.printStackTrace();
        }
    }
}
