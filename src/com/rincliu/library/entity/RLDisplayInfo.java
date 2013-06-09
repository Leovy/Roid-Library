package com.rincliu.library.entity;

import java.io.Serializable;

public class RLDisplayInfo implements Serializable{
	private static final long serialVersionUID = 4348988349135775928L;
	
	private int displayWidth;
	private int displayHeight;
	private float displayDensity;
	private int statusBarHeight;
	private int portraitNavigationBarHeight;
	private int landscapeNavigationBarHeight;
	public int getDisplayWidth() {
		return displayWidth;
	}
	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}
	public int getDisplayHeight() {
		return displayHeight;
	}
	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}
	public float getDisplayDensity() {
		return displayDensity;
	}
	public void setDisplayDensity(float displayDensity) {
		this.displayDensity = displayDensity;
	}
	public int getStatusBarHeight() {
		return statusBarHeight;
	}
	public void setStatusBarHeight(int statusBarHeight) {
		this.statusBarHeight = statusBarHeight;
	}
	public int getPortraitNavigationBarHeight() {
		return portraitNavigationBarHeight;
	}
	public void setPortraitNavigationBarHeight(int portraitNavigationBarHeight) {
		this.portraitNavigationBarHeight = portraitNavigationBarHeight;
	}
	public int getLandscapeNavigationBarHeight() {
		return landscapeNavigationBarHeight;
	}
	public void setLandscapeNavigationBarHeight(int landscapeNavigationBarHeight) {
		this.landscapeNavigationBarHeight = landscapeNavigationBarHeight;
	}
}