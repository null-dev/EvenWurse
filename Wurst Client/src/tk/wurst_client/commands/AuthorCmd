package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import tk.wurst_client.commands.Cmd.Info;

@Info (help="Sets the held written book's author to specified word.",
name="author",
syntax = {"<author>"})

public class AuthorCmd extends Cmd {

	@Override
	public void execute(String[] args) throws Cmd.Error {
	
	String author = "";
	ItemStack currentItem;
	Item currentItemAsItem;
	final int writtenBookId = 387;
    
    if(args.length != 1) {
    	error("Syntax Error.");
    }
    else {
               
    	author = args[0];
                
    	//Makes sure that player is in Creative mode.
        if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
        	error("Creative mode only.");
        }
        else {
       	         
        	//Makes sure that player is holding an item.
     	    if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null) {
     	        error("Please execute this command while holding a written book.");
            }
            else {
         		//Gets current item in player's hand.
         	    currentItem = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        	        	
         	    //Gets the Item from currentItem.
         	    currentItemAsItem = currentItem.getItem();
       	             	
        	    //Ensures that held item is a book.
                if(Item.getIdFromItem(currentItemAsItem) != writtenBookId) {
                	error("Please execute this command while holding a written book.");
                }
                else {
                	//Sets book's author to String author.
                    currentItem.setTagInfo("author", new NBTTagString(author));
            	}
            }
    	}
    }
 	
    }
}
