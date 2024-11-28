# SpammieWhammie

**A projected I did during the pandemic to learn more about Android development. Definitely PTSD levels of design pattern overload.**

## History of Architectural Changes
- Will give up on the JavaScript based input update. Instead, the Do Not Call website **report.js** will be reverse engineered. See reportJS.md for notes on reverse engineering.
- Change from multi-activity to single activity
  - Thought it would help with the speed of running the base JavaScript. However, it wasn't the problem.
  - Single Activity is the best structure because there's no need for multiple tasks for this application.
- Biggest problem is dealing with getting the JavaScript to run properly. However, nothing really works right. 
  - Seems like the issue is that there's actually a ClearData function in the **report.js** file.
