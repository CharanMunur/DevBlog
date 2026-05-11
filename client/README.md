# Devblog Client

This is the frontend application for the Devblog project. It is a modern single-page application built for speed and developer experience.

## Tech Stack

- **Framework**: [React 19](https://react.dev/) + [Vite](https://vitejs.dev/)
- **Styling**: [Tailwind CSS v4](https://tailwindcss.com/)
- **Components**: [shadcn/ui](https://ui.shadcn.com/) (Accessible, customizable components)
- **State Management**: [Zustand](https://zustand-demo.pmnd.rs/)
- **Theming**: `next-themes` (Dark/Light mode support)

## Development Setup

1. Install dependencies:
   ```bash
   bun install
   ```

2. Start the development server:
   ```bash
   bun run dev
   ```

3. Build for production:
   ```bash
   bun run build
   ```

## Folder Structure

- `src/components/` - Reusable UI components (like shadcn/ui components).
- `src/App.tsx` - The main application entry point and layout routing.
- `src/index.css` - Global CSS configurations, including Tailwind v4 configuration.
