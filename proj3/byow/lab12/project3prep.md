# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: The provided implementation uses helper classes like Position and more helper methods than my own, which I think creates better layers of abstraction and makes the code much cleaner and easier to understand.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: Adding each hexagon into the tessellation is just like adding individual room or hallway piece into the entire world.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: I would start by writing methods to draw individual components, as well as corresponding helper methods for like calculating positions, to build a good abstraction layer.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: A hallway has more than an exits, while a room only has one.
