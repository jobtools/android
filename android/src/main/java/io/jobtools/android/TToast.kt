package io.jobtools.android

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import android.view.*
import android.widget.Toast
import java.lang.ref.WeakReference


/**
 * @param context The context to use.  Usually your [Application]
 * or [Activity] object.
 */

class TToast : Toast(context) {

    companion object {

        private var lastToast: WeakReference<Toast>? = null

        /**
         * Make a custom toast that just contains a text view.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        private fun makeText(context: Context, text: CharSequence, duration: Int): Toast {
            clearLastOne()

            val toast = createToast(
                context,
                text,
                duration
            )
            lastToast = WeakReference(toast)
            return toast
        }

        private fun createToast(context: Context, text: CharSequence, duration: Int): Toast =
            Toast.makeText(context, text, duration)

        private val context: Context
            get() {
                return AppInstance.get()
            }

        @JvmStatic
        fun show(o: Any) {

            if (o is Throwable) {
                show(
                    context, o.message
                        ?: "Null message", LENGTH_LONG
                )
            } else {
                show(
                    context,
                    o.toString(),
                    LENGTH_SHORT
                )
            }
        }

        @JvmStatic
        fun show(text: CharSequence) {
            show(
                context,
                text,
                LENGTH_SHORT
            )
        }

        @JvmStatic
        fun show(@StringRes stringId: Int) {
            show(
                context.getString(
                    stringId
                )
            )
        }


        @JvmStatic
        fun showWithFlag(active: Boolean, text: CharSequence) {
            if (active) {
                show(text)
            }
        }

        /**
         * Make a custom toast that just contains a text view.
         *
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        @JvmStatic
        fun show(text: CharSequence, duration: Int) {
            show(
                context,
                text,
                duration
            )
        }

        @JvmStatic
        fun show(@StringRes stringId: Int, duration: Int) {
            show(
                context.getString(
                    stringId
                ), duration
            )
        }


        /**
         * Make a custom toast that just contains a text view.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        @JvmStatic
        fun show(context: Context, text: CharSequence, duration: Int) {
            Handler(Looper.getMainLooper())
                .post {
                    makeText(
                        context,
                        text,
                        duration
                    ).show()
                    TLog.d("#TOAST", text.toString())
                }
        }

        /**
         * Make a custom toast that just contains a text view.
         *
         * @param text The text to show.  Can be formatted text.
         */
        @JvmStatic
        fun showLong(text: CharSequence) {
            show(
                context,
                text,
                LENGTH_LONG
            )
        }

        @JvmStatic
        fun showLong(@StringRes stringId: Int) {
            showLong(
                context.getString(
                    stringId
                )
            )
        }

        @JvmStatic
        fun clearLastOne() {
            lastToast?.get()?.cancel()
            lastToast = null
        }

        var debugEnabled = false

        @JvmStatic
        fun debugEnable(enable: Boolean) {
            debugEnabled = enable
        }

        @JvmStatic
        fun showDebug(debugMessage: CharSequence) {
            if (debugEnabled) {
                show("DEBUG:$debugMessage")
            }
        }

        @JvmStatic
        fun showDebug(t: Throwable) {
            if (debugEnabled) {
                t.printStackTrace()
                TLog.e(t)
                show("DEBUG: Throwable: ${t.message}")
            }
        }

    }

}
