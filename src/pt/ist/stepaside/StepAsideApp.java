package pt.ist.stepaside;


import android.app.Application;
import android.content.Context;

public class StepAsideApp extends Application {

	private static StepAsideApp instance;

	public StepAsideApp() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}
}
