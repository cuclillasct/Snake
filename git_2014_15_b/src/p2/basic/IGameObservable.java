package p2.basic;

public interface IGameObservable extends IGame {
	public void addObserver(IGameObserver obs);
	public void removeObserver(IGameObserver obs);
}
