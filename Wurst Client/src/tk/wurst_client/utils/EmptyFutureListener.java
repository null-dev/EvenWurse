package tk.wurst_client.utils;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class EmptyFutureListener implements GenericFutureListener
{
	@Override
	public void operationComplete(Future arg0) throws Exception
	{
		
	}	
	
}
