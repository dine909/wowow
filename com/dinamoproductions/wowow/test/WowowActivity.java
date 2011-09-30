package com.dinamoproductions.wowow.test;

import java.io.*;
import com.dinamoproductions.wowow.server.*;
import com.dinamoproductions.wowow.server.http.*;
import com.dinamoproductions.wowow.server.http.handlers.*;
import com.dinamoproductions.wowow.test.R;

import android.app.Activity;
import android.os.Bundle;

public class WowowActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
			Server s= new Server(8888);
			HttpSocketHandler httpSocketHandler=new HttpSocketHandler();
			
			//serve files
			FileSystemHttpHandler fileSystemHandler = new FileSystemHttpHandler(
					(HttpHeaderMatcher) new PathMatcher("/files/*"),new File("/"));
			fileSystemHandler.allowDirectoryBrowsing=true;
			httpSocketHandler.addHandler(fileSystemHandler);
			
			//serve errors
			httpSocketHandler.addHandler(new DefaultHttpHandler((HttpHeaderMatcher) new PathMatcher("/*")));
			
			s.setSocketHandler(httpSocketHandler);
			s.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}