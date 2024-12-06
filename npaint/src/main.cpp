// Copyright 2020 Arthur Sonzogni. All rights reserved.
// Use of this source code is governed by the MIT license that can be found in
// the LICENSE file.
/*#include <chrono>		  // for operator""s, chrono_literals*/
#include <cstdlib>		  // for system
#include <ftxui/dom/elements.hpp> // for Element, operator|, separator, filler, hbox, size, spinner, text, vbox, bold, border, Fit, EQUAL, WIDTH
#include <ftxui/screen/screen.hpp> // for Full, Screen
#include <iostream>		   // for cout, endl, ostream
#include <string>		   // for to_string, operator<<, string
#include <thread>		   // for sleep_for
#include <utility>		   // for move
#include <vector>		   // for vector

#include "ftxui/dom/node.hpp" // for Render
/*#include "ftxui/screen/color.hpp" // for ftxui*/

int
main()
{
	using namespace ftxui;
	using namespace std::chrono_literals;

	system("./caffeinemode on");

	auto screen = Screen::Create(Dimension::Full(), Dimension::Full());
	std::string reset_position = screen.ResetPosition();

	for (int index = 0; index < 200; index = (index + 1) % 200) {
		std::vector<Element> entries;
		for (int i = 0; i < 23; ++i) {
			if (i != 0)
				entries.push_back(separator());
			entries.push_back(window(text(std::to_string(i)) | size(WIDTH, EQUAL, 2), spinner(i, index) | bold));
		}
		auto style = size(WIDTH, GREATER_THAN, 20) | border | size(HEIGHT, GREATER_THAN, 30) | size(WIDTH, LESS_THAN, 50);
		auto document = hbox({
			hflow(std::move(entries)) | style,
		});
		Render(screen, document);
		std::cout << reset_position;
		screen.Print();

		std::this_thread::sleep_for(0.1s);
	}
	std::cout << std::endl;

	system("./caffeinemode off");
}
