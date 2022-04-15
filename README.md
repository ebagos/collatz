# コラッツの数列の長さを計算するサーバ

- GO及びKotlinで作成
    - Kotlin側はkotlin/exampleベースに作成したため、諸々の記述が膨大になっている
- どちらもサーバソフトはGitHub Actionsにてビルドし、GitHub Container Registry(ghcr.io）にイメージを格納する
    - ビルド方法の違いはコンテナをマルチステージビルドしているため、GitHub Actionsのスクリプトは同一（Matrix Buildを利用）
    - なお、GO言語側はBazelの設定を残しているが、うまく動かないため使用していない