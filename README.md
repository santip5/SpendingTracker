# \# SpendingTracker

# 

# A simple desktop spending tracker built with \*\*Java 17\*\* and \*\*Swing\*\*.

# 

# \## Features

# 

# \- Add expenses with:

#   - Date (spinner)

#   - Amount

#   - Payment method (Debit / Credit / Savings)

#   - Tags (e.g. groceries, rent, etc.)

# \- View expenses in a sortable table

# \- Filter by:

#   - Date range

#   - Payment method

#   - One or more tags (exact match)

# \- See total of currently visible rows

# \- Save/load expenses to/from a CSV file

# 

# \## Tech Stack

# 

# \- Java 17

# \- Swing (NetBeans GUI Builder)

# \- Custom `Expense`, `ExpenseRepository`, and `ExpenseStorage` classes

# \- Launch4j for Windows `.exe` packaging

# 

# \## Running from source

# 

# Requires \*\*Java 17+\*\* and \*\*Ant\*\* (NetBeans can handle this automatically).

# 

# ```bash

# git clone https://github.com/your-username/SpendingTracker.git

# cd SpendingTracker

# ant jar

# java -jar dist/SpendingTracker.jar

