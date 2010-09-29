package com.android.droidlicious.activity;

import com.android.droidlicious.Constants;
import com.android.droidlicious.R;
import com.android.droidlicious.client.NetworkUtilities;
import com.android.droidlicious.client.User;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBookmark extends Activity implements View.OnClickListener{

	private EditText mEditUrl;
	private EditText mEditDescription;
	private EditText mEditNotes;
	private Button mButtonSave;
	private AccountManager mAccountManager;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_bookmark);
		mEditUrl = (EditText) findViewById(R.id.add_edit_url);
		mEditDescription = (EditText) findViewById(R.id.add_edit_description);
		mEditNotes = (EditText) findViewById(R.id.add_edit_notes);
		mButtonSave = (Button) findViewById(R.id.add_button_save);

		mButtonSave.setOnClickListener(this);	
	}
	
    public void save() {
    	
		mAccountManager = AccountManager.get(this);
		Account[] al = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
		String authtoken = null;
		Boolean success = false;
		
		User.Bookmark bookmark = new User.Bookmark(mEditUrl.getText().toString(), 
				mEditDescription.getText().toString(), mEditNotes.getText().toString());
		
		try {
			
			AccountManagerFuture<Bundle> accountManagerFuture = 
				mAccountManager.getAuthToken(al[0], Constants.AUTHTOKEN_TYPE, null, this, null, null);
			Bundle authTokenBundle = accountManagerFuture.getResult(); 
			
			authtoken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
		} catch (Exception e1) {
			Log.d("blash", e1.getMessage());
		}
    	
    	try {
			success = NetworkUtilities.addBookmarks(bookmark, al[0], authtoken, this);
		} catch (Exception e) {
			//Log.d("add bookmark error", e.getMessage());
			//Log.d("add bookmark error", e.toString());
		}
		
		if(success){
			Toast.makeText(getApplicationContext(), "Bookmark Added Successfully", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}
		
		finish();
        
    }

	
    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        if (v == mButtonSave) {
            save();
        }
    }

}
