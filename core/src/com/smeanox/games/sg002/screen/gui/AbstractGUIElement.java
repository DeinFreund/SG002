package com.smeanox.games.sg002.screen.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * A clickable object
 * @author Benjamin Schmid
 */
public abstract class AbstractGUIElement implements GUIElement {
	private LinkedList<ClickHandler> clickHandlers;
	protected Rectangle boundingBox;

	protected boolean visible;
	protected boolean active;

	protected Vector2 position;
	protected Vector2 size;

	protected Resizer resizer;

	private boolean wasDown = false;
	protected Vector2 lastTouchPos;

	public AbstractGUIElement(){
		size = new Vector2(0, 0);
		position = new Vector2(0, 0);
		boundingBox = new Rectangle();
		visible = true;
		active = true;
		lastTouchPos = new Vector2();
	}

	/**
	 * called once per frame to check if the object is clicked
	 * @param wasClick true if there was already a click in this frame.
	 * @return true if the element was clicked
	 */
	@Override
	public boolean updateClickable(Vector2 touchPos, boolean wasClick) {
		boolean wasClicked = false;
		if(visible && !wasClick && wasDown && !Gdx.input.isTouched()){
			if(boundingBox != null && boundingBox.contains(touchPos.x, touchPos.y)) {
				if(active) {
					fireOnClick();
				}
				wasClicked = true;
			}
		}

		wasDown = Gdx.input.isTouched();

		lastTouchPos.set(touchPos);

		return wasClicked;
	}

	protected void fireOnClick(){
		if(clickHandlers != null) {
			for (ClickHandler c : clickHandlers) {
				c.onClick();
			}
		}
	}

	/**
	 * adds a {@link ClickHandler}
	 *
	 * @param handler the ClickHandler
	 */
	@Override
	public void addClickHandler(ClickHandler handler) {
		if(clickHandlers == null){
			clickHandlers = new LinkedList<ClickHandler>();
		}
		clickHandlers.add(handler);
	}

	/**
	 * removes a {@link ClickHandler}
	 *
	 * @param handler the ClickHandler
	 */
	@Override
	public void removeClickHandler(ClickHandler handler) {
		if(clickHandlers == null){
			return;
		}
		clickHandlers.remove(handler);
		if(clickHandlers.isEmpty()){
			clickHandlers = null;
		}
	}

	/**
	 * Returns the bounding box of this object
	 * @return the bounding box
	 */
	@Override
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Sets the bounding box of this object
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void setBoundingBox(float x, float y, float width, float height){
		boundingBox.set(x, y, width, height);
		setSize(boundingBox.getWidth(), boundingBox.getHeight());
		setPosition(boundingBox.getX(), boundingBox.getY());
	}

	/**
	 * Sets tho bounding box of this object
	 * @param rectangle the bounding box
	 */
	protected void setBoundingBox(Rectangle rectangle){
		boundingBox.set(rectangle);
		setSize(boundingBox.getWidth(), boundingBox.getHeight());
		setPosition(boundingBox.getX(), boundingBox.getY());
	}

	/**
	 * Sets the size of this object
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height){
		size.set(width, height);
		boundingBox.setSize(width, height);
	}

	/**
	 * Sets the position of this object
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		position.set(x, y);
		boundingBox.setPosition(x, y);
	}

	/**
	 * Sets the center of this object
	 * @param x
	 * @param y
	 */
	public void setCenter(float x, float y) {
		boundingBox.setCenter(x, y);
		position.set(boundingBox.getX(), boundingBox.getY());
	}

	public Resizer getResizer() {
		return resizer;
	}

	public void setResizer(Resizer resizer) {
		this.resizer = resizer;
	}

	/**
	 * Updates the position and size of the object
	 *
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(float width, float height) {
		setBoundingBox(resizer.getNewSize(width, height));
	}
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}


	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
