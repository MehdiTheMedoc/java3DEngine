package fr.medoc.main.game.collisions;

public interface collisionDetectionAlgorithm {
	public void collisionCheck();
	public void addCollider(Collider collider);
	public void removeCollider(Collider collider);
}
