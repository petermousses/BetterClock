# GitHub Issue Handling Skill

A structured workflow for addressing GitHub issues using Claude Code and the `gh` CLI.

## Overview

This skill provides a step-by-step process to:
1. View a GitHub issue and extract context
2. Sync with the latest develop branch
3. Create an issue-specific branch
4. Develop changes using issue context
5. Open a pull request back to develop

## Prerequisites

- `gh` CLI installed and authenticated (`gh auth status`)
- Git installed and configured
- Working directory is a Git repository

## Workflow Steps

### Step 1: View and Extract Issue Context

**Command:**
```bash
gh issue view <ISSUE_NUMBER> --json title,body,labels
```

**What this does:**
- Retrieves the issue title, description, and labels
- Formats output as JSON for easy parsing
- Provides all context needed for development

**Example:**
```bash
gh issue view 42 --json title,body,labels
```

**Output to save:**
- Issue title (becomes your primary objective)
- Issue description (detailed requirements)
- Labels/tags (indicates priority, type, etc.)

### Step 2: Pull Latest Develop Branch

**Commands:**
```bash
git fetch origin
git checkout develop
git pull origin develop
```

**What this does:**
- Fetches latest changes from remote
- Switches to develop branch
- Updates local develop with latest remote version

**Why:** Ensures your work is based on the most recent code

### Step 3: Create Issue-Specific Branch

**Command:**
```bash
git checkout -b issue/<ISSUE_NUMBER>
```

**What this does:**
- Creates new branch named `issue/<ISSUE_NUMBER>`
- Automatically switches to the new branch
- Branches from current develop HEAD

**Example:**
```bash
git checkout -b issue/42
```

**Convention:**
- Use lowercase numbers
- Use forward slash as separator
- Prefix must be `issue/`

### Step 4: Use Issue Context as Development Prompt

Gather the issue information from Step 1:

```bash
TITLE=$(gh issue view <ISSUE_NUMBER> --json title -q '.title')
BODY=$(gh issue view <ISSUE_NUMBER> --json body -q '.body')
LABELS=$(gh issue view <ISSUE_NUMBER> --json labels -q '.labels[].name' | tr '\n' ', ')
```

**Construct your development prompt with:**

```
Issue #<ISSUE_NUMBER>: [TITLE]

Description:
[BODY]

Labels: [LABELS]

Requirements:
- [Extract key requirements from description]
- [Any acceptance criteria]
- [Specific implementation notes]

When complete, create a pull request with:
- Title: "Fix/Feature: [TITLE] (#<ISSUE_NUMBER>)"
- Description: Link to issue #<ISSUE_NUMBER>
- Any additional testing or validation steps
```

**Best Practices:**
- Include the full issue description verbatim when possible
- Highlight acceptance criteria and requirements
- Note any labels indicating priority or type
- Reference the issue number in all commits

### Step 5: Develop and Commit

Work on the issue using the context from Step 4. Make meaningful commits:

```bash
git add .
git commit -m "Fix: Brief description of change (#<ISSUE_NUMBER>)"
```

**Commit message conventions:**
- Start with type: `Fix:`, `Feature:`, `Refactor:`, `Docs:`, etc.
- Reference issue number in parentheses
- Keep first line under 72 characters

### Step 6: Open Pull Request Back to Develop

**Command:**
```bash
gh pr create --base develop --title "<PR_TITLE>" --body "<PR_BODY>"
```

**What this does:**
- Creates PR targeting develop branch
- Uses your PR title and description
- Automatically links to your issue branch

**Example:**
```bash
gh pr create \
  --base develop \
  --title "Fix: Implement challenge system for alarms (#42)" \
  --body "Closes #42

## Changes
- Implemented math challenge generation
- Added challenge validation
- Integrated with alarm trigger flow

## Testing
- Unit tests for challenge logic pass
- Integration tests pass
- Manual testing on Android emulator verified"
```

**PR Best Practices:**
- Title should reference the issue
- Use `Closes #<ISSUE_NUMBER>` to auto-link
- Include summary of changes
- List any testing performed
- Note any breaking changes

## Complete Workflow Command Sequence

```bash
# View issue
gh issue view <ISSUE_NUMBER> --json title,body,labels

# Sync with develop
git fetch origin
git checkout develop
git pull origin develop

# Create issue branch
git checkout -b issue/<ISSUE_NUMBER>

# [Develop and commit your changes here]

# Open PR
gh pr create \
  --base develop \
  --title "Fix/Feature: [Your Title] (#<ISSUE_NUMBER>)" \
  --body "Closes #<ISSUE_NUMBER>

## Changes
- [List changes]

## Testing
- [List testing steps]"
```

## Useful gh CLI Commands Reference

```bash
# View specific issue with all details
gh issue view <ISSUE_NUMBER>

# List open issues
gh issue list --state open

# List issues with specific label
gh issue list --label "bug"

# View PR details
gh pr view <PR_NUMBER>

# List PRs awaiting review
gh pr list --state open --search "review:pending"

# Check PR status
gh pr status

# View PR comments
gh pr view <PR_NUMBER> --comments
```

## Troubleshooting

**Issue: `gh: not found`**
- Install GitHub CLI: https://cli.github.com/
- Verify installation: `gh --version`

**Issue: `gh: authentication required`**
- Login: `gh auth login`
- Verify: `gh auth status`

**Issue: Branch already exists**
- Delete and recreate: `git branch -D issue/<ISSUE_NUMBER>` then create new

**Issue: PR failed to create**
- Ensure you have commits on your branch: `git log --oneline origin/develop..`
- Verify branch is pushed: `git push -u origin issue/<ISSUE_NUMBER>`
- Check you have write access to repository

## Tips for Success

1. **Always reference the issue number** in commits and PR - enables automatic linking
2. **Use descriptive titles** - future developers should understand changes from title
3. **Test before opening PR** - run relevant tests locally first
4. **Keep branches focused** - one issue per branch, one PR per issue
5. **Write clear commit messages** - include why, not just what changed
6. **Check the issue description again** before marking complete - ensure all requirements met
7. **Link related issues** in PR description if applicable

## Integration with Claude Code

Use this workflow within Claude Code sessions:

1. Start session: `claude-code`
2. View issue: `gh issue view <ISSUE_NUMBER> --json title,body,labels`
3. Run sync commands via bash tool
4. Create branch and begin work
5. Use issue context in your development prompts
6. Commit changes with Claude Code tools
7. Create PR and link back to issue

This ensures all development is tied to specific issues with full traceability.
