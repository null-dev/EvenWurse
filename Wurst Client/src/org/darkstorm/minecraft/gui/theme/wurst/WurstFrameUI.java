/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.darkstorm.minecraft.gui.theme.wurst;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

public class WurstFrameUI extends AbstractComponentUI<Frame>
{
	private final WurstTheme theme;
	
	WurstFrameUI(WurstTheme theme)
	{
		super(Frame.class);
		this.theme = theme;
		
		foreground = Color.WHITE;
		background = new Color(64, 64, 64, 128);
	}
	
	@Override
	protected void renderComponent(Frame component)
	{
		Rectangle area = new Rectangle(component.getArea());
		int fontHeight = theme.getFontRenderer().FONT_HEIGHT;
		translateComponent(component, false);
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glShadeModel(GL_SMOOTH);
		if(component.isMinimized())
			area.height = fontHeight + 4;
		
		// title bar background
		glColor4f(0.03125f, 0.03125f, 0.03125f, 0.5f);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(area.width, 0);
			glVertex2d(area.width, fontHeight + 4);
			glVertex2d(0, fontHeight + 4);
		}
		glEnd();
		
		// frame background
		RenderUtil.setColor(background);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, fontHeight + 4);
			glVertex2d(area.width, fontHeight + 4);
			glVertex2d(area.width, area.height);
			glVertex2d(0, area.height);
		}
		glEnd();
		RenderUtil.boxShadow(0, 0, area.width, area.height);
		
		// title bar icons
		int offset = component.getWidth() - 2;
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = component;
		while(parent != null)
		{
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		boolean[] checks =
			new boolean[]{component.isClosable(), component.isPinnable(),
				component.isMinimizable()};
		boolean[] overlays =
			new boolean[]{component.isClosable(), !component.isPinned(),
				component.isMinimized()};
		for(int i = 0; i < checks.length; i++)
		{
			if(!checks[i])
				continue;
			
			// icon background
			glColor4f(0f, 0f, 0f, 0.25f);
			glBegin(GL_QUADS);
			{
				glVertex2d(offset - fontHeight, 2);
				glVertex2d(offset, 2);
				glVertex2d(offset, fontHeight + 2);
				glVertex2d(offset - fontHeight, fontHeight + 2);
			}
			glEnd();
			RenderUtil.boxShadow(offset - fontHeight, 2, offset, fontHeight + 2);
			
			// pin button
			if(i == 1 && overlays[i])
			{
				// if not pinned
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3, 2);
					glVertex2d(offset, fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3, fontHeight / 3 * 2 + 2);
					glVertex2d(offset - fontHeight / 3 * 2, fontHeight / 3 + 2);
				}
				glEnd();
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 1,
						fontHeight / 3 + 1);
					glVertex2d(offset - fontHeight / 3 + 1,
						fontHeight / 3 * 2 + 3);
					glVertex2d(offset - fontHeight / 3, fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 * 2 - 2,
						fontHeight / 3 + 2);
				}
				glEnd();
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3 * 2, fontHeight / 3 + 4);
					glVertex2d(offset - fontHeight / 3 - 2,
						fontHeight / 3 * 2 + 2);
					glVertex2d(offset - fontHeight + 1.5, fontHeight + 0.5);
				}
				glEnd();
				glColor4f(0f, 1f, 0f, 0.25f);
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 3, 2);
					glVertex2d(offset, fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3, fontHeight / 3 * 2 + 2);
					glVertex2d(offset - fontHeight / 3 * 2, fontHeight / 3 + 2);
				}
				glEnd();
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 1,
						fontHeight / 3 + 1);
					glVertex2d(offset - fontHeight / 3 + 1,
						fontHeight / 3 * 2 + 3);
					glVertex2d(offset - fontHeight / 3, fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 * 2 - 2,
						fontHeight / 3 + 2);
				}
				glEnd();
				glColor4f(0.75f, 0.75f, 0.75f, 0.75f);
				glBegin(GL_TRIANGLES);
				{
					glVertex2d(offset - fontHeight / 3 * 2, fontHeight / 3 + 4);
					glVertex2d(offset - fontHeight / 3 - 2,
						fontHeight / 3 * 2 + 2);
					glVertex2d(offset - fontHeight + 1.5, fontHeight + 0.5);
				}
				glEnd();
				if(mouse.x >= offset - fontHeight
					&& mouse.x <= offset
					&& mouse.y >= 2
					&& mouse.y <= fontHeight + 2
					&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
				{
					glColor4f(0f, 1f, 0f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 3, 2);
						glVertex2d(offset, fontHeight / 3 + 2);
						glVertex2d(offset - fontHeight / 3,
							fontHeight / 3 * 2 + 2);
						glVertex2d(offset - fontHeight / 3 * 2,
							fontHeight / 3 + 2);
					}
					glEnd();
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 3 * 2 - 1,
							fontHeight / 3 + 1);
						glVertex2d(offset - fontHeight / 3 + 1,
							fontHeight / 3 * 2 + 3);
						glVertex2d(offset - fontHeight / 3,
							fontHeight / 3 * 2 + 4);
						glVertex2d(offset - fontHeight / 3 * 2 - 2,
							fontHeight / 3 + 2);
					}
					glEnd();
					glColor4f(1f, 1f, 1f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_TRIANGLES);
					{
						glVertex2d(offset - fontHeight / 3 * 2,
							fontHeight / 3 + 4);
						glVertex2d(offset - fontHeight / 3 - 2,
							fontHeight / 3 * 2 + 2);
						glVertex2d(offset - fontHeight + 1.5, fontHeight + 0.5);
					}
					glEnd();
				}
			}else if(i == 1)
			{// If it is pinned:
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
						fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3 + 0.5,
						fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3 + 0.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
						fontHeight / 3 * 2 + 4);
				}
				glEnd();
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 + 1.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 + 1.5,
						fontHeight / 3 * 2 + 5);
					glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
						fontHeight / 3 * 2 + 5);
				}
				glEnd();
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight / 3 * 2,
						fontHeight / 3 * 2 + 5.1);
					glVertex2d(offset - fontHeight / 3 * 2 + 1.5,
						fontHeight / 3 * 2 + 5.1);
					glVertex2d(offset - fontHeight / 3 * 2 + 1.3,
						fontHeight + 2);
					glVertex2d(offset - fontHeight / 3 * 2 + 0.2,
						fontHeight + 2);
				}
				glEnd();
				glColor4f(1f, 0f, 0f, 0.25f);
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
						fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3 + 0.5,
						fontHeight / 3 + 2);
					glVertex2d(offset - fontHeight / 3 + 0.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
						fontHeight / 3 * 2 + 4);
				}
				glEnd();
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 + 1.5,
						fontHeight / 3 * 2 + 4);
					glVertex2d(offset - fontHeight / 3 + 1.5,
						fontHeight / 3 * 2 + 5);
					glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
						fontHeight / 3 * 2 + 5);
				}
				glEnd();
				glColor4f(0.75f, 0.75f, 0.75f, 0.75f);
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 3 * 2,
						fontHeight / 3 * 2 + 5.1);
					glVertex2d(offset - fontHeight / 3 * 2 + 1.5,
						fontHeight / 3 * 2 + 5.1);
					glVertex2d(offset - fontHeight / 3 * 2 + 1.3,
						fontHeight + 2);
					glVertex2d(offset - fontHeight / 3 * 2 + 0.2,
						fontHeight + 2);
				}
				glEnd();
				if(mouse.x >= offset - fontHeight
					&& mouse.x <= offset
					&& mouse.y >= 2
					&& mouse.y <= fontHeight + 2
					&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
				{
					glColor4f(1f, 0f, 0f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
							fontHeight / 3 + 2);
						glVertex2d(offset - fontHeight / 3 + 0.5,
							fontHeight / 3 + 2);
						glVertex2d(offset - fontHeight / 3 + 0.5,
							fontHeight / 3 * 2 + 4);
						glVertex2d(offset - fontHeight / 3 * 2 - 1.5,
							fontHeight / 3 * 2 + 4);
					}
					glEnd();
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
							fontHeight / 3 * 2 + 4);
						glVertex2d(offset - fontHeight / 3 + 1.5,
							fontHeight / 3 * 2 + 4);
						glVertex2d(offset - fontHeight / 3 + 1.5,
							fontHeight / 3 * 2 + 5);
						glVertex2d(offset - fontHeight / 3 * 2 - 2.5,
							fontHeight / 3 * 2 + 5);
					}
					glEnd();
					glColor4f(1f, 1f, 1f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 3 * 2,
							fontHeight / 3 * 2 + 5.1);
						glVertex2d(offset - fontHeight / 3 * 2 + 1.5,
							fontHeight / 3 * 2 + 5.1);
						glVertex2d(offset - fontHeight / 3 * 2 + 1.3,
							fontHeight + 2);
						glVertex2d(offset - fontHeight / 3 * 2 + 0.2,
							fontHeight + 2);
					}
					glEnd();
				}
			}
			if(i == 2 && overlays[i])// UI for the minimize button:
			{// If it is minimized:
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight + 1, 4.5);
					glVertex2d(offset - 1, 4.5);
					glVertex2d(offset - fontHeight / 2, fontHeight - 0.5);
				}
				glEnd();
				glColor4f(0f, 1f, 0f, 0.25f);
				glBegin(GL_TRIANGLES);
				{
					glVertex2d(offset - fontHeight + 1, 4.5);
					glVertex2d(offset - 1, 4.5);
					glVertex2d(offset - fontHeight / 2, fontHeight - 0.5);
				}
				glEnd();
				if(mouse.x >= offset - fontHeight
					&& mouse.x <= offset
					&& mouse.y >= 2
					&& mouse.y <= fontHeight + 2
					&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
				{
					glColor4f(0f, 1f, 0f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_TRIANGLES);
					{
						glVertex2d(offset - fontHeight + 1, 4.5);
						glVertex2d(offset - 1, 4.5);
						glVertex2d(offset - fontHeight / 2, fontHeight - 0.5);
					}
					glEnd();
				}
			}else if(i == 2)
			{// If it is not minimized:
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight + 1, fontHeight - 1);
					glVertex2d(offset - 1, fontHeight - 1);
					glVertex2d(offset - fontHeight / 2, 4);
				}
				glEnd();
				glColor4f(1f, 0f, 0f, 0.25f);
				glBegin(GL_TRIANGLES);
				{
					glVertex2d(offset - fontHeight + 1, fontHeight - 1);
					glVertex2d(offset - 1, fontHeight - 1);
					glVertex2d(offset - fontHeight / 2, 4);
				}
				glEnd();
				if(mouse.x >= offset - fontHeight
					&& mouse.x <= offset
					&& mouse.y >= 2
					&& mouse.y <= fontHeight + 2
					&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
				{
					glColor4f(1f, 0f, 0f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_TRIANGLES);
					{
						glVertex2d(offset - fontHeight + 1, fontHeight - 1);
						glVertex2d(offset - 1, fontHeight - 1);
						glVertex2d(offset - fontHeight / 2, 4);
					}
					glEnd();
				}
			}
			if(i == 0)// UI for the close button:
			{
				glLineWidth(1f);
				glColor4f(0f, 0f, 0f, 1f);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2d(offset - fontHeight, 4);
					glVertex2d(offset - fontHeight + 2, 2);
					glVertex2d(offset - fontHeight / 2, fontHeight / 2);
					glVertex2d(offset - 2, 2);
					glVertex2d(offset, 4);
					glVertex2d(offset - fontHeight / 2 + 2, fontHeight / 2 + 2);
					glVertex2d(offset, fontHeight);
					glVertex2d(offset - 2, fontHeight + 2);
					glVertex2d(offset - fontHeight / 2, fontHeight / 2 + 4);
					glVertex2d(offset - fontHeight + 2, fontHeight + 2);
					glVertex2d(offset - fontHeight, fontHeight);
					glVertex2d(offset - fontHeight / 2 - 2, fontHeight / 2 + 2);
				}
				glEnd();
				glColor4f(1f, 0f, 0f, 0.25f);
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight, 4);
					glVertex2d(offset - fontHeight + 2, 2);
					glVertex2d(offset, fontHeight);
					glVertex2d(offset - 2, fontHeight + 2);
				}
				glEnd();
				glBegin(GL_QUADS);
				{
					glVertex2d(offset, 4);
					glVertex2d(offset - 2, 2);
					glVertex2d(offset - fontHeight / 2, fontHeight / 2);
					glVertex2d(offset - fontHeight / 2 + 2, fontHeight / 2 + 2);
				}
				glEnd();
				glBegin(GL_QUADS);
				{
					glVertex2d(offset - fontHeight / 2, fontHeight / 2 + 4);
					glVertex2d(offset - fontHeight / 2 - 2, fontHeight / 2 + 2);
					glVertex2d(offset - fontHeight, fontHeight);
					glVertex2d(offset - fontHeight + 2, fontHeight + 2);
				}
				glEnd();
				if(mouse.x >= offset - fontHeight
					&& mouse.x <= offset
					&& mouse.y >= 2
					&& mouse.y <= fontHeight + 2
					&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
				{
					glColor4f(1f, 0f, 0f, Mouse.isButtonDown(0) ? 0.375f
						: 0.25f);
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight, 4);
						glVertex2d(offset - fontHeight + 2, 2);
						glVertex2d(offset, fontHeight);
						glVertex2d(offset - 2, fontHeight + 2);
					}
					glEnd();
					glBegin(GL_QUADS);
					{
						glVertex2d(offset, 4);
						glVertex2d(offset - 2, 2);
						glVertex2d(offset - fontHeight / 2, fontHeight / 2);
						glVertex2d(offset - fontHeight / 2 + 2,
							fontHeight / 2 + 2);
					}
					glEnd();
					glBegin(GL_QUADS);
					{
						glVertex2d(offset - fontHeight / 2, fontHeight / 2 + 4);
						glVertex2d(offset - fontHeight / 2 - 2,
							fontHeight / 2 + 2);
						glVertex2d(offset - fontHeight, fontHeight);
						glVertex2d(offset - fontHeight + 2, fontHeight + 2);
					}
					glEnd();
				}
			}
			offset -= fontHeight + 2;
		}
		
		// title bar
		if(!component.isMinimized())
			RenderUtil
				.downShadow(0, fontHeight + 4, area.width, fontHeight + 5);
		glEnable(GL_TEXTURE_2D);
		theme.getFontRenderer().drawStringWithShadow(component.getTitle(), 2,
			2, RenderUtil.toRGBA(component.getForegroundColor()));
		
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(component, true);
	}
	
	@Override
	protected Rectangle getContainerChildRenderArea(Frame container)
	{
		Rectangle area = new Rectangle(container.getArea());
		area.x = 2;
		area.y = theme.getFontRenderer().FONT_HEIGHT + 6;
		area.width -= 4;
		area.height -= theme.getFontRenderer().FONT_HEIGHT + 8;
		return area;
	}
	
	@Override
	protected Dimension getDefaultComponentSize(Frame component)
	{
		Component[] children = component.getChildren();
		Rectangle[] areas = new Rectangle[children.length];
		Constraint[][] constraints = new Constraint[children.length][];
		for(int i = 0; i < children.length; i++)
		{
			Component child = children[i];
			Dimension size =
				child.getTheme().getUIForComponent(child).getDefaultSize(child);
			areas[i] = new Rectangle(0, 0, size.width, size.height);
			constraints[i] = component.getConstraints(child);
		}
		Dimension size =
			component.getLayoutManager().getOptimalPositionedSize(areas,
				constraints);
		size.width += 4;
		size.height += theme.getFontRenderer().FONT_HEIGHT + 8;
		return size;
	}
	
	@Override
	protected Rectangle[] getInteractableComponentRegions(Frame component)
	{
		return new Rectangle[]{new Rectangle(0, 0, component.getWidth(),
			theme.getFontRenderer().FONT_HEIGHT + 4)};
	}
	
	@Override
	protected void handleComponentInteraction(Frame component, Point location,
		int button)
	{
		if(button != 0)
			return;
		int offset = component.getWidth() - 2;
		int textHeight = theme.getFontRenderer().FONT_HEIGHT;
		if(component.isClosable())
		{
			if(location.x >= offset - textHeight && location.x <= offset
				&& location.y >= 2 && location.y <= textHeight + 2)
			{
				component.close();
				return;
			}
			offset -= textHeight + 2;
		}
		if(component.isPinnable())
		{
			if(location.x >= offset - textHeight && location.x <= offset
				&& location.y >= 2 && location.y <= textHeight + 2)
			{
				component.setPinned(!component.isPinned());
				return;
			}
			offset -= textHeight + 2;
		}
		if(component.isMinimizable())
		{
			if(location.x >= offset - textHeight && location.x <= offset
				&& location.y >= 2 && location.y <= textHeight + 2)
			{
				component.setMinimized(!component.isMinimized());
				return;
			}
			offset -= textHeight + 2;
		}
		if(location.x >= 0 && location.x <= offset && location.y >= 0
			&& location.y <= textHeight + 4)
		{
			component.setDragging(true);
			return;
		}
	}
}
