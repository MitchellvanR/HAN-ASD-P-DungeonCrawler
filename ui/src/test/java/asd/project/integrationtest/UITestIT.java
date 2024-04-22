package asd.project.integrationtest;

import asd.project.UI;
import asd.project.domain.event.Event;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui-integration-test")
@DisplayName("Integration tests for UI")
public class UITestIT {

  UI sut;

  @BeforeEach
  public void setup() {
    sut = new UI();
  }

  @Test
  @DisplayName("UIObservableIT_TestObservable")
  void UIObservableTest1() {
    //Assert & Act
    Assertions.assertTrue(sut.getObservable() instanceof Observable<Event>);
  }


}
