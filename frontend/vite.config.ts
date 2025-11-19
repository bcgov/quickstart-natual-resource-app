import { resolve } from 'node:path';

import react from '@vitejs/plugin-react';
import { defineConfig, loadEnv } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';
import { configDefaults } from 'vitest/config';

export default defineConfig(({ mode }) => {
  const define = {
    global: {},
  };
  return {
    define,
    resolve: {
      alias: {
        //Debatable
        '@': resolve(__dirname, 'src'),
      },
    },
    plugins: [
      react(),
      tsconfigPaths(),
    ],
    build: {
      chunkSizeWarningLimit: 1024,
      outDir: 'build',
    },
    optimizeDeps: {
      include: [
        '@tanstack/react-query',
        'aws-amplify',
        'aws-amplify/auth/cognito',
        'aws-amplify/utils',
        'react-dom/client',
        '@tanstack/react-query-devtools',
        'aws-amplify/auth',
      ],
    },
    server: {
      port: 3000,
      hmr: {
        overlay: false,
      },
    },
    preview: {
      port: 3000,
    },
    test: {
      env: loadEnv(mode, process.cwd(), ''),
      exclude: [...configDefaults.exclude, 'dist/**', 'build/**'],
      globals: true,
      tsconfig: './tsconfig.test.json',
      reporters: ['default', ['junit', { suiteName: 'Unit tests' }]],
      outputFile: {
        junit: './coverage/junit-report.xml',
      },
      coverage: {
        provider: 'v8',
        reporter: ['lcov', 'cobertura', 'html', 'json', 'text'],
        reportsDirectory: './coverage',
        all: true,
        exclude: [
          '**/node_modules/**',
          '**/tests/**',
          '**/*.test.{ts,tsx}',
          '**/vite-env.d.ts',
          '**/types/**',
          '**/constants/**',
          '**/config/fam/*',
          '**/config/react-query/*',
          '**/config/tests/*',
          '**/*.env.ts',
          '**/*.scss',
          '**/*.css',
          '**/*.d.ts',
          '**/types.ts',
          '**/*.types.ts',
          '**/main.tsx',
          '**/App.tsx',
        ],
        include: ['src/**/*.ts', 'src/**/*.tsx'],
        lines: 80,
        functions: 80,
        branches: 80,
        statements: 80,
      },
      projects: [
        {
          name: 'node',
          extends: true,
          test: {
            setupFiles: [
              './src/config/tests/setup-env.ts',
              './src/config/tests/custom-matchers.ts',
            ],
            environment: 'jsdom',
            include: ['src/**/*.unit.test.{ts,tsx}'],
          },
        },
      ],
    },
  };
});
