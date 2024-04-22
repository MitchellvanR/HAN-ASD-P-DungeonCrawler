package asd.project;

import asd.project.domain.event.Event;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * A EventObservable is the link between different submodules. Each observable has a different
 * EventType
 */
public abstract class EventObservable {

  protected final PublishSubject<Event> publishSubject;

  protected EventObservable() {
    publishSubject = PublishSubject.create();
  }

  public Observable<Event> getObservable() {
    return publishSubject;
  }

  public void publish(Event event) {
    publishSubject.onNext(event);
  }
}
