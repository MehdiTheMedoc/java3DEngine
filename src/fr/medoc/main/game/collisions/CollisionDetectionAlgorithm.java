package fr.medoc.main.game.collisions;

public interface CollisionDetectionAlgorithm {
	public void collisionCheck();
	public void addCollider(Collider collider);
	public void removeCollider(Collider collider);
}
