// Copyright 2021 Arthur Sonzogni. All rights reserved.
// Use of this source code is governed by the MIT license that can be found in
// the LICENSED file.
#include <cmath>		   // for sin, cos
#include <ftxui/dom/elements.hpp>  // for canvas, Element, separator, hbox, operator|, border
#include <ftxui/screen/screen.hpp> // for Pixel
#include <memory>		   // for allocator, shared_ptr, __shared_ptr_access
#include <string>		   // for string, basic_string
#include <utility>		   // for move
#include <vector>		   // for vector, __alloc_traits<>::value_type

#include "ftxui/component/component.hpp"	  // for Renderer, CatchEvent, Horizontal, Menu, Tab
#include "ftxui/component/component_base.hpp"	  // for ComponentBase
#include "ftxui/component/event.hpp"		  // for Event
#include "ftxui/component/mouse.hpp"		  // for Mouse
#include "ftxui/component/screen_interactive.hpp" // for ScreenInteractive
#include "ftxui/dom/canvas.hpp"			  // for Canvas
#include "ftxui/screen/color.hpp"		  // for Color, Color::Red, Color::Blue, Color::Green, ftxui
	//

using namespace ftxui;

Element
ColorTile(int red, int green, int blue)
{
	return text("") | size(WIDTH, GREATER_THAN, 14) | size(HEIGHT, GREATER_THAN, 7) | bgcolor(Color::RGB(red, green, blue));
}

Element
ColorString(int red, int green, int blue)
{
	return text("RGB = (" +			  //
		    std::to_string(red) + "," +	  //
		    std::to_string(green) + "," + //
		    std::to_string(blue) + ")"	  //
	);
}

int
main()
{
	int mouse_x = 0;
	int mouse_y = 0;

	int red = 128;
	int green = 25;
	int blue = 100;
	auto slider_red = Slider("Red  :", &red, 0, 255, 1);
	auto slider_green = Slider("Green:", &green, 0, 255, 1);
	auto slider_blue = Slider("Blue :", &blue, 0, 255, 1);

	auto sidebar = Container::Vertical({
		slider_red,
		slider_green,
		slider_blue,
	});

	auto sidebar_renderer = Renderer(sidebar, [&] {
		return vbox({
			ColorTile(red, green, blue),
			separator(),
			slider_red->Render(),
			separator(),
			slider_green->Render(),
			separator(),
			slider_blue->Render(),
			separator(),
			ColorString(red, green, blue),
		});
	});

	auto renderer_line_block = Renderer([&] {
		auto c = Canvas(100, 100);
		c.DrawText(0, 0, "Several lines (block)");
		c.DrawBlockLine(mouse_x, mouse_y, 80, 10, Color::Red);
		c.DrawBlockLine(80, 10, 80, 40, Color::Blue);
		c.DrawBlockLine(80, 40, mouse_x, mouse_y, Color::Green);
		return hbox({ canvas(std::move(c)) | borderDashed }) | center;
	});

	int selected_tab = 0;
	auto tab = Container::Tab(
		{
			renderer_line_block,
		},
		&selected_tab);

	// This capture the last mouse position.
	auto tab_with_mouse = tab | CatchEvent([&](Event e) {
				      if (e.is_mouse()) {
					      mouse_x = (e.mouse().x - 1) * 2;
					      mouse_y = (e.mouse().y - 1) * 4;
				      }
				      return false;
			      });

	// the container
	auto component = Container::Horizontal({
		sidebar,
		tab_with_mouse,
	});

	// Tell (modify) the container how to display its content
	// Add some separator to decorate the whole component:
	auto component_renderer = Renderer(component, [&] {
		return hbox({
			       sidebar_renderer->Render() | size(WIDTH, LESS_THAN, 80),
			       separator(),
			       tab_with_mouse->Render() | xflex,
		       }) |
		       border;
	});

	auto screen = ScreenInteractive::Fullscreen();
	screen.Loop(component_renderer);

	return 0;
}
