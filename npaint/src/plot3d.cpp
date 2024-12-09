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

int
main()
{
	using namespace ftxui;

	int mouse_x = 0;
	int mouse_y = 0;

	auto renderer_plot_3 = Renderer([&] {
		auto c = Canvas(200, 100);
		c.DrawText(0, 0, "A 2D gaussian plot");
		int size = 15;

		// mouse_x = 5mx + 3*my
		// mouse_y = 0mx + -5my + 90
		// this part is alpine transforming the mouse coordinate into 3d
		float my = (mouse_y - 90) / -5.f;
		float mx = (mouse_x - 3 * my) / 5.f;
		std::vector<std::vector<float>> ys(size, std::vector<float>(size));
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				float dx = x - mx;
				float dy = y - my;
				ys[y][x] = -1.5 + 3.0 * std::exp(-0.2f * (dx * dx + dy * dy));
			}
		}
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (x != 0) {
					c.DrawPointLine(5 * (x - 1) + 3 * (y - 0),
							90 - 5 * (y - 0) - 5 * ys[y][x - 1],
							5 * (x - 0) + 3 * (y - 0),
							90 - 5 * (y - 0) - 5 * ys[y][x]);
				}
				if (y != 0) {
					c.DrawPointLine(5 * (x - 0) + 3 * (y - 1),
							90 - 5 * (y - 1) - 5 * ys[y - 1][x],
							5 * (x - 0) + 3 * (y - 0),
							90 - 5 * (y - 0) - 5 * ys[y][x]);
				}
			}
		}

		return canvas(std::move(c));
	});

	int selected_tab = 0;
	auto tab = Container::Tab(
		{
			renderer_plot_3,
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

	std::vector<std::string> tab_titles = {
		"plot_3 3D",
	};
	auto tab_toggle = Menu(&tab_titles, &selected_tab);

    // the container 
	auto component = Container::Horizontal({
		tab_with_mouse,
		tab_toggle,
	});

    // Tell (modify) the container how to display its content
	// Add some separator to decorate the whole component:
	auto component_renderer = Renderer(component, [&] {
		return hbox({
			       tab_with_mouse->Render() | border,
			       separator(),
			       tab_toggle->Render(),
		       }) |
		       border;
	});

	auto screen = ScreenInteractive::Fullscreen();
	screen.Loop(component_renderer);

	return 0;
}
