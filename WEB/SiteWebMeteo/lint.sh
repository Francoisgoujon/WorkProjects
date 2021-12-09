#!/bin/bash
if [ "$1" = "pre-commit" ]; then
    if [[ ! -z $(git diff --cached --name-only | grep -E '\.(js|ts)$') ]]; then
        echo 'Organize imports for js/ts files'
        organize-imports-cli $(git diff --cached --name-only | grep -E '\.(js|ts)$')
        # above command may change staged files, so need to add them again
        git add $(git diff --cached --name-only | grep -E '\.(js|ts)$')
    fi
    echo 'Format with prettier'
    pretty-quick --staged
elif [ "$1" = "pre-push" ] && [[ ! -z $(git diff-tree --no-commit-id --name-only -r HEAD | grep -E '\.(js|ts)$') ]]; then
    echo 'Pre-push hook: linting all js/ts files in the last commit'
    ng lint gti525-labo --files "{`git diff-tree --no-commit-id --name-only -r HEAD | grep -E "\.(js|ts)$" | paste -sd "," -`}"
fi
