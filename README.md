# TodoListApp 🎯

## Overview 🚀
TodoListApp is a task management application built with Android Studio and Java. It empowers users to organize and track their daily activities by creating personal and shared to-do lists, managing tasks, and collaborating with others.

## Key Features ✨
- **User Registration & Login** 🔐  
  - Register with a unique username, password (minimum 5 characters), name, surname, and birthdate.  
  - Secure login using username and password.

- **Todo List Management** 📋  
  - Create, rename, or delete lists with unique names.  
  - Categorize lists into:  
    - **Personal:** Owned by the user.  
    - **Shared with Me:** Lists shared by others.  
    - **Shared:** Lists the user has shared.

- **Task Management** 📝  
  - Create tasks with a title, description, priority (LOW, MEDIUM, HIGH), and deadline.  
  - Schedule tasks as daily or for the next day, week, or month.  
  - Mark tasks as completed, moving them to a "Done" section.  
  - Highlight overdue tasks in red, affecting both the task and its list.  
  - Delete tasks using a swipe gesture.

- **Sharing & Permissions** 🤝  
  - **Shared Lists:**  
    - Only the owner can add or delete tasks, manage participants, or delete the list.  
    - Participants can mark tasks as completed (credited with their username).  
  - **Invitations:**  
    - Owners invite others via username.  
    - Recipients can accept or decline the invitation.  
    - Participants can leave shared lists, while owners can remove participants.

- **Deletion Rules** 🗑️  
  - Users can delete their personal lists or leave shared lists.  
  - Participants cannot delete tasks in shared lists.

- **User Interface** 💻  
  - **Home Screen:**  
    - **Personal:** Displays all user-owned lists.  
    - **Shared with Me:** Displays lists shared by others.  
    - **Shared:** Displays lists the user has shared.  
  - Lists are auto-sorted into "Personal" or "Shared" sections when added.  
  - Adding tasks involves specifying the title, description, priority, and deadline.

- **Database Schema (ER Diagram)** 🗂️  
  - **User:** username (unique), password, name, surname, birthdate.  
  - **List:** title (unique), owner (linked to User), isShared (boolean).  
  - **Task:** description, priority, deadline, isDone (boolean).  
  - **SharedList:** Manages the relationships between Users and Lists.

- **Additional Notes** 🛠️  
  - **Real-time Updates:** Shared lists refresh on access or via a manual refresh button.  
  - **Error Handling:** Overdue tasks and lists are highlighted in red.

## Installation 🛠️
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/MatteoConcutelli/TodoListApp.git
2. **Open in Android Studio**
- Launch Android Studio.
- Select "Open an existing Android Studio project" and navigate to the cloned repository.
3. **Build & Run**
- Build the project and run it on an emulator or a physical Android device.**

## Usage 📲

### Register & Login
- Create a new account with your details, then log in to access your personalized dashboard.

### Manage Lists & Tasks
- Create, rename, or delete lists.
- Add tasks with detailed information and manage them via intuitive UI gestures.

### Collaborate
- Share lists with other users by sending invitations.
- Manage permissions and track task completion collaboratively.

## Technologies Used 💻
- **Android Studio**
- **Java SDK**
- **SQLite** (for local data storage)  
*(Additional libraries and frameworks can be listed here)*
