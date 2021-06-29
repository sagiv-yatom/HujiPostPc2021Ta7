package huji.postpc.y2021.rachelsandwiches;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.TestCase;

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;

@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 29)
public class RachelSandwichesTest extends TestCase {

    @Test
    public void when_newOrderActivityIsLaunched_then_allFieldsInitCorrectly() {
        NewOrderActivity newOrderActivity = Robolectric.buildActivity(NewOrderActivity.class).create().visible().get();
        EditText customerNameEditText = newOrderActivity.findViewById(R.id.customerNameEditText);
        TextView picklesNum = newOrderActivity.findViewById(R.id.picklesNumTextView);
        Button hummusButton = newOrderActivity.findViewById(R.id.hummusButton);
        Button tahiniButton = newOrderActivity.findViewById(R.id.tahiniButton);
        EditText commentEditText = newOrderActivity.findViewById(R.id.commentEditText);

        assertEquals(customerNameEditText.getText().toString(), "");
        assertEquals(picklesNum.getText().toString(), "0");
        assertEquals(hummusButton.getText().toString(), "No");
        assertEquals(tahiniButton.getText().toString(), "No");
        assertEquals(commentEditText.getText().toString(), "");
    }

    @Test
    public void when_clickForMoreThan10Pickles_then_buttonIsDisabled() {
        NewOrderActivity newOrderActivity = Robolectric.buildActivity(NewOrderActivity.class).create().visible().get();
        Button increasePicklesNumButton = newOrderActivity.findViewById(R.id.increasePicklesNumButton);
        increasePicklesNumButton.performClick(); // 1
        increasePicklesNumButton.performClick(); // 2
        increasePicklesNumButton.performClick(); // 3
        increasePicklesNumButton.performClick(); // 4
        increasePicklesNumButton.performClick(); // 5
        increasePicklesNumButton.performClick(); // 6
        increasePicklesNumButton.performClick(); // 7
        increasePicklesNumButton.performClick(); // 8
        increasePicklesNumButton.performClick(); // 9
        increasePicklesNumButton.performClick(); // 10

        assertFalse(increasePicklesNumButton.isEnabled());
    }

    @Test
    public void when_clickOnHummusButton_then_theTextChangedSuccessfully() {
        NewOrderActivity newOrderActivity = Robolectric.buildActivity(NewOrderActivity.class).create().visible().get();
        Button hummusButton = newOrderActivity.findViewById(R.id.hummusButton);
        hummusButton.performClick();

        assertEquals(hummusButton.getText().toString(), "Yes");
    }

}
