package org.hkrdi.eden.ggm.vaadin.console.etl.v2;
public class GameObject{

		public String name;
		public Transform transform = new Transform();

		public Componentt GetComponent() {
			return new Componentt();
		}

		public GameObject() {
		}
		public GameObject(GameObject nodePrefab) {
			// TODO Auto-generated method stub
			super();
		}

		public void SetActive(boolean activate) {
			// TODO Auto-generated method stub
			
		}

		public StarModel GetComponentStarModel() {
			// TODO Auto-generated method stub
			return null;
		}}
