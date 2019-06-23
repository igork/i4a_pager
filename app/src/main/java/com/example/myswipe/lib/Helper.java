package com.example.myswipe.lib;

import android.content.Context;
import android.support.annotation.NonNull;

// https://stackoverflow.com/questions/2002288/static-way-to-get-context-in-android
/*


Assuming we're talking about getting the Application Context, I implemented it as suggested by @Rohit Ghatol extending Application. What happened then,
it's that there's no guarantee that the context retrieved in such a way will always be non-null. At the time you need it, it's usually because you want
to initialize an helper, or get a resource, that you cannot delay in time; handling the null case will not help you. So I understood I was basically
fighting against the Android architecture, as stated in the docs

    Note: There is normally no need to subclass Application. In most situations, static singletons can provide the same functionality in a more modular way.
    If your singleton needs a global context (for example to register broadcast receivers), include Context.getApplicationContext() as a Context argument
    when invoking your singleton's getInstance() method.

and explained by Dianne Hackborn

    The only reason Application exists as something you can derive from is because during the pre-1.0 development one of our application developers was
    continually bugging me about needing to have a top-level application object they can derive from so they could have a more "normal" to them application model,
    and I eventually gave in. I will forever regret giving in on that one. :)

She is also suggesting the solution to this problem:

    If what you want is some global state that can be shared across different parts of your app, use a singleton. [...] And this leads more naturally to how
    you should be managing these things -- initializing them on demand.

so what I did was getting rid of extending Application, and pass the context directly to the singleton helper's getInstance(), while saving a reference to the
application context in the private constructor:

Helper.getInstance(myCtx).doSomething();

So, to answer this question properly: there are ways to access the Application Context statically, but they all should be discouraged, and you should prefer
passing a local context to the singleton's getInstance().
 */
public class Helper {

    private static Helper instance;
    private final Context mContext;

    private Helper(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    public static Helper getInstance(@NonNull Context context) {
        synchronized(Helper.class) {
            if (instance == null) {
                instance = new Helper(context);
            }
            return instance;
        }
    }
}
