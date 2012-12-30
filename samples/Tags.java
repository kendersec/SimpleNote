import java.util.Arrays;

import org.kender.simplenote.STag;
import org.kender.simplenote.SimpleNote;
import org.kender.simplenote.exceptions.ConnectionFailed;


public class Tags {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("You need more arguments");
            return;
        }
        
        String email = args[0];
        String password = args[1];
        
        SimpleNote sn = new SimpleNote(email, password);
        
        try {
            printTags(sn);
            
            // Adding a tag
            STag newTag = sn.addTag("test1");
            sn.addTag("test2");
            printTags(sn);
            
            // Updating the tag
            newTag.setName("Test1");
            newTag = sn.updateTag(newTag);
            printTags(sn);
            
            // Deleting the tag
            sn.deleteTag(newTag);
            sn.deleteTag("test2");
            printTags(sn);
        } catch (ConnectionFailed e) {
            e.printStackTrace();
        }
    }

    private static void printTags(SimpleNote sn) throws ConnectionFailed {
        // Printing my tags
        STag[] tags = sn.getTags();
        System.out.println("My tags: " + Arrays.toString(tags));
    }
}
